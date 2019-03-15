package org.money_transfer.service.repository

import org.money_transfer.service.model.Account
import spock.lang.Specification

/**
 * @author Maksym Bondarenko
 * @version 1.0 15.03.19
 */

class InMemoryAccountRepositoryTest extends Specification {

    def repository = new InMemoryAccountRepository()

    def "should throw IllegalArgumentException on null account in putAccount"() {
        when:
        repository.putAccount(null)

        then:
        thrown IllegalArgumentException
    }

    def "should successfully put account"() {
        given:
        def accountNumber = 12345

        when:
        repository.putAccount(new Account('account1', accountNumber, 100.5G))

        then:
        repository.isPresent(accountNumber)
    }

    def "should successfully retrieve account"() {
        given:
        def accountNumber = 12345
        def expectedAccount = new Account('account1', accountNumber, 100.5G)
        repository.putAccount(expectedAccount)

        when:
        def actualAccount = repository.getAccount(accountNumber)

        then:
        actualAccount == expectedAccount
    }
}
