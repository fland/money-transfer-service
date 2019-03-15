package org.money_transfer.service.repository

import org.money_transfer.service.model.TransferState
import spock.lang.Specification

import static org.money_transfer.service.model.TransferState.State.ACCEPTED
import static org.money_transfer.service.model.TransferState.State.PROCESSED

/**
 * @author Maksym Bondarenko
 * @version 1.0 15.03.19
 */

class InMemoryTransferStateRepositoryTest extends Specification {

    def repository = new InMemoryTransferStateRepository()

    def "should throw IllegalArgumentException on null transferState in putState"() {
        when:
        repository.putState(null)

        then:
        thrown IllegalArgumentException
    }

    def "should throw IllegalArgumentException on null trasferUuid in getState"() {
        when:
        repository.getState(null)

        then:
        thrown IllegalArgumentException
    }

    def "should successfully put and retrieve transfer state"() {
        given:
        def transferUuid = '1'
        def expectedState = new TransferState(transferUuid, 12345, 1234, 12.3G, PROCESSED)
        repository.putState(expectedState)

        when:
        def actualState = repository.getState(transferUuid)

        then:
        actualState == expectedState
    }

    def "should successfully retrieve account states"() {
        given:
        def accountNumber = 12345
        def expectedStates = [new TransferState('1', 12345, 12344, 12.3G, PROCESSED),
                              new TransferState('2', 12344, 12345, 14.3G, ACCEPTED)]
        expectedStates.each { i -> repository.putState(i) }
        repository.putState(new TransferState('3', 12346, 12344, 12.3G, PROCESSED))

        when:
        def actualStates = repository.getStatesByAccount(accountNumber)

        then:
        actualStates.size() == expectedStates.size()
        actualStates.containsAll(expectedStates)
    }
}
