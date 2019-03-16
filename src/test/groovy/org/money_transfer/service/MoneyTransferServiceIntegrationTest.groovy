package org.money_transfer.service

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.Rule
import org.junit.contrib.java.lang.system.EnvironmentVariables
import org.money_transfer.service.model.Account
import org.money_transfer.service.model.Transfer
import org.money_transfer.service.model.TransferState
import spock.lang.Specification
import spock.util.concurrent.PollingConditions

import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

import static javax.ws.rs.core.HttpHeaders.CONTENT_TYPE
import static javax.ws.rs.core.HttpHeaders.LOCATION
import static javax.ws.rs.core.MediaType.APPLICATION_JSON
import static javax.ws.rs.core.Response.Status.CREATED
import static javax.ws.rs.core.Response.Status.OK
import static org.money_transfer.service.model.TransferState.State.PROCESSED

/**
 * @author Maksym Bondarenko
 * @version 1.0 16.03.19
 */

class MoneyTransferServiceIntegrationTest extends Specification {

    @Rule
    EnvironmentVariables env = new EnvironmentVariables()

    def objectMapper = new ObjectMapper()
    def service = new MoneyTransferService()
    def httpClient = HttpClient.newHttpClient()

//    TODO: looks more like a cucumber test
    def "should process transaction"() {
        given:
        env.set('SERVER_PORT', '0')
        def server = service.start()
        def port = server.getURI().getPort()
        def accountUrl = "http://localhost:$port/api/account"
        def startProcessingUrl = "http://localhost:$port/api/processing/start"
        def transferUrl = "http://localhost:$port/api/transfer"
        def sourceAccountNumber = 12345
        def destinationAccountNumber = 12344
        def sourceAccount = new Account('name1', sourceAccountNumber, 34.5G)
        def destinationAccount = new Account('name2', destinationAccountNumber, 0.0G)
        def transferAmount = 13.45G

        sendAccount(sourceAccount, httpClient, accountUrl)
        sendAccount(destinationAccount, httpClient, accountUrl)

        httpClient.send(HttpRequest
                .newBuilder()
                .GET()
                .uri(URI.create(startProcessingUrl))
                .build(), HttpResponse.BodyHandlers.ofString())

        def transferResponse = httpClient.send(HttpRequest
                .newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(
                        objectMapper.writeValueAsString(
                                new Transfer(sourceAccountNumber, destinationAccountNumber, transferAmount)
                        )))
                .header(CONTENT_TYPE, APPLICATION_JSON)
                .uri(URI.create(transferUrl))
                .build(), HttpResponse.BodyHandlers.ofString())
        def transfer = objectMapper.readValue(transferResponse.body(), TransferState.class)

        when:
        waitForTransactionProcessing(transfer.uuid, httpClient, transferUrl)
        def finalSourceAccount = getAccount(sourceAccountNumber, httpClient, accountUrl)
        def finalDestinationAccount = getAccount(destinationAccountNumber, httpClient, accountUrl)

        then:
        finalDestinationAccount.balance == destinationAccount.balance.add(transferAmount)
        finalSourceAccount.balance == sourceAccount.balance.subtract(transferAmount)

        cleanup:
        server.stop()
    }

    def "should create account"() {
        given:
        env.set('SERVER_PORT', '0')
        def server = service.start()
        def accountNumber = 12345
        def port = server.getURI().getPort()
        def url = "http://localhost:$port/api/account"
        def account = new Account('name1', accountNumber, 34.5G)

        when:
        def response = sendAccount(account, httpClient, url)

        then:
        response.statusCode() == CREATED.statusCode
        response.headers().firstValue(LOCATION).isPresent()
        response.headers().firstValue(LOCATION).get() == "$url/$accountNumber"

        cleanup:
        server.stop()
    }

    def "should retrieve account"() {
        given:
        env.set('SERVER_PORT', '0')
        def server = service.start()
        def accountNumber = 12345
        def port = server.getURI().getPort()
        def url = "http://localhost:$port/api/account"
        def account = new Account('name1', accountNumber, 34.5G)
        sendAccount(account, httpClient, url)

        when:
        def request = HttpRequest
                .newBuilder()
                .GET()
                .uri(URI.create("$url/$accountNumber"))
                .build()
        def response = httpClient.send(request, HttpResponse.BodyHandlers.ofString())

        then:
        response.statusCode() == OK.statusCode
        response.headers().firstValue(CONTENT_TYPE).isPresent()
        response.headers().firstValue(CONTENT_TYPE).get() == APPLICATION_JSON
        objectMapper.readValue(response.body(), Account.class) == account

        cleanup:
        server.stop()
    }

    private def waitForTransactionProcessing(String transferUuid, HttpClient httpClient, String url) {
        def conditions = new PollingConditions(timeout: 10, initialDelay: 1.5, factor: 1.25)
        conditions.eventually {
            assert objectMapper.readValue(httpClient.send(HttpRequest
                    .newBuilder()
                    .GET()
                    .uri(URI.create("$url/$transferUuid"))
                    .build(), HttpResponse.BodyHandlers.ofString()).body(), TransferState.class).state == PROCESSED
        }
    }

    private def getAccount(long accountNumber, HttpClient httpClient, String url) {
        objectMapper.readValue(httpClient.send(HttpRequest
                .newBuilder()
                .GET()
                .uri(URI.create("$url/$accountNumber"))
                .build(), HttpResponse.BodyHandlers.ofString()).body(), Account.class)
    }

    private def sendAccount(Account account, HttpClient httpClient, String url) {
        httpClient.send(HttpRequest
                .newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(account)))
                .uri(URI.create(url))
                .header(CONTENT_TYPE, APPLICATION_JSON)
                .build(), HttpResponse.BodyHandlers.ofString())
    }
}
