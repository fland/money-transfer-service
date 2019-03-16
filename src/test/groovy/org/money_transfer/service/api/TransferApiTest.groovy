package org.money_transfer.service.api

import org.money_transfer.service.exception.ApiRequestValidationException
import org.money_transfer.service.exception.ResourceNotFoundException
import org.money_transfer.service.model.Account
import org.money_transfer.service.model.Transfer
import org.money_transfer.service.model.TransferState
import org.money_transfer.service.repository.AccountRepository
import org.money_transfer.service.repository.TransferStateRepository
import org.money_transfer.service.service.TransferQueueService
import org.money_transfer.service.service.TransferUuidProvider
import spock.lang.Specification

import javax.validation.ConstraintViolation
import javax.validation.Validator
import javax.ws.rs.core.Response

import static org.money_transfer.service.model.TransferState.State.ACCEPTED
import static org.money_transfer.service.model.TransferState.State.PROCESSED

/**
 * @author Maksym Bondarenko
 * @version 1.0 16.03.19
 */

class TransferApiTest extends Specification {

    def accountRepository = Mock(AccountRepository.class)
    def transferQueueService = Mock(TransferQueueService.class)
    def transferStateRepository = Mock(TransferStateRepository.class)
    def validator = Stub(Validator.class)
    def transferUuidProvider = Stub(TransferUuidProvider.class)
    def transferApi = new TransferApi(accountRepository, transferQueueService, transferStateRepository, validator, transferUuidProvider)

    def "should return transactions for account"() {
        given:
        def accountNumber = 12345
        def expectedTransfers = [new TransferState('1', 12345, 12344, 12.5G, PROCESSED),
                                 new TransferState('1', 12344, 12345, 16.5G, ACCEPTED)] as Set


        when:
        def transfers = transferApi.getTransfersByAccount(accountNumber)

        then:
        1 * transferStateRepository.getStatesByAccount(accountNumber) >> expectedTransfers

        and:
        transfers.size() == expectedTransfers.size()
        transfers.containsAll(expectedTransfers)
    }

    def "should throw ApiRequestValidationException on invalid request while postTransfer"() {
        given:
        def sourceAccount = 12345
        def destinationAccount = 12344
        def amount = 34.5G
        def transfer = new Transfer(sourceAccount, destinationAccount, amount)

        when:
        transferApi.postTransfer(transfer)

        then:
        validator.validate(transfer) >> [Stub(ConstraintViolation.class)]

        and:
        thrown ApiRequestValidationException
    }

    def "should successfully post transfer"() {
        given:
        def sourceAccount = 12345
        def destinationAccount = 12344
        def transferUuid = 'uuid1'
        def amount = 34.5G

        when:
        def response = transferApi.postTransfer(new Transfer(sourceAccount, destinationAccount, amount))

        then:
        1 * accountRepository.getAccount(sourceAccount) >> new Account('', sourceAccount, 5.0G)
        1 * accountRepository.getAccount(destinationAccount) >> new Account('', destinationAccount, 100.0G)
        transferUuidProvider.provideUuid() >> transferUuid
        1 * transferStateRepository.putState(TransferState
                .builder()
                .state(ACCEPTED)
                .sourceAccount(sourceAccount)
                .destinationAccount(destinationAccount)
                .amount(amount)
                .uuid(transferUuid)
                .build())
        1 * transferQueueService.put(transferUuid) >> true

        and:
        response.status == Response.Status.ACCEPTED.statusCode
    }

    def "should ResourceNotFoundException on no destination account in repository while postTransfer"() {
        given:
        def sourceAccount = 12345
        def destinationAccount = 12344
        def amount = 34.5G

        when:
        transferApi.postTransfer(new Transfer(sourceAccount, destinationAccount, amount))

        then:
        1 * accountRepository.getAccount(sourceAccount) >> new Account('', sourceAccount, 5.0G)
        1 * accountRepository.getAccount(destinationAccount) >> null

        and:
        thrown ResourceNotFoundException
    }

    def "should ResourceNotFoundException on no source account in repository while postTransfer"() {
        given:
        def sourceAccount = 12345
        def destinationAccount = 12344
        def amount = 34.5G

        when:
        transferApi.postTransfer(new Transfer(sourceAccount, destinationAccount, amount))

        then:
        1 * accountRepository.getAccount(sourceAccount) >> null

        and:
        thrown ResourceNotFoundException
    }
}
