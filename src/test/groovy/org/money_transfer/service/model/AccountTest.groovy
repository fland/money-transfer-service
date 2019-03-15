package org.money_transfer.service.model

import spock.lang.Specification
import spock.lang.Unroll

import javax.validation.Validation

/**
 * @author Maksym Bondarenko
 * @version 1.0 15.03.19
 */

class AccountTest extends Specification {

    def validator = Validation.buildDefaultValidatorFactory().getValidator()

    @Unroll
    def "should generate validation violation on illegal account number #accountNumber"() {
        given:
        def account = new Account('name1', accountNumber, 4.5G)

        when:
        def violations = validator.validate(account)

        then:
        !violations.isEmpty()

        where:
        accountNumber << [9999, 100000, null]
    }

    @Unroll
    def "should generate validation violation on illegal balance #balance"() {
        given:
        def account = new Account('name2', 10001, balance)

        when:
        def violations = validator.validate(account)

        then:
        !violations.isEmpty()

        where:
        balance << [null, -1.5G]
    }

}
