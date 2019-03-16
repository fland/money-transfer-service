package org.money_transfer.service

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.Rule
import org.junit.contrib.java.lang.system.EnvironmentVariables
import org.money_transfer.service.model.Account
import spock.lang.Specification

import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

import static javax.ws.rs.core.HttpHeaders.CONTENT_TYPE
import static javax.ws.rs.core.HttpHeaders.LOCATION
import static javax.ws.rs.core.MediaType.APPLICATION_JSON
import static javax.ws.rs.core.Response.Status.CREATED
import static javax.ws.rs.core.Response.Status.OK

/**
 * @author Maksym Bondarenko
 * @version 1.0 16.03.19
 */

class MoneyTransferServiceIntegrationTest extends Specification {

    @Rule
    EnvironmentVariables env = new EnvironmentVariables()

    def objectMapper = new ObjectMapper()

    def "should create account"() {
        given:
        env.set('SERVER_PORT', '0')
        def service = new MoneyTransferService()
        def server = service.start()
        def httpClient = HttpClient.newHttpClient()
        def accountNumber = 12345
        def port = server.getURI().getPort()
        def url = "http://localhost:$port/api/account"
        def account = new Account('name1', accountNumber, 34.5G)

        when:
        def request = HttpRequest
                .newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(account)))
                .uri(URI.create(url))
                .header(CONTENT_TYPE, APPLICATION_JSON)
                .build()
        def response = httpClient.send(request, HttpResponse.BodyHandlers.ofString())

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
        def service = new MoneyTransferService()
        def server = service.start()
        def httpClient = HttpClient.newHttpClient()
        def accountNumber = 12345
        def port = server.getURI().getPort()
        def url = "http://localhost:$port/api/account"
        def account = new Account('name1', accountNumber, 34.5G)
        httpClient.send(HttpRequest
                .newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(account)))
                .uri(URI.create(url))
                .header(CONTENT_TYPE, APPLICATION_JSON)
                .build(), HttpResponse.BodyHandlers.ofString())


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
}
