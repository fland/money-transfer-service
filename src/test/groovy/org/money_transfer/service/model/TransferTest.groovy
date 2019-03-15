package org.money_transfer.service.model

import spock.lang.Specification
import spock.lang.Unroll

import javax.validation.Validation

/**
 * @author Maksym Bondarenko
 * @version 1.0 15.03.19
 */

class TransferTest extends Specification {

    def validator = Validation.buildDefaultValidatorFactory().getValidator()

    @Unroll
    def "should generate validation violation on illegal source account number #sourceAccount"() {
        given:
        def transfer = new Transfer(sourceAccount, 10001, 4.5G)

        when:
        def violations = validator.validate(transfer)

        then:
        !violations.isEmpty()

        where:
        sourceAccount << [9999, 100000, null]
    }

    @Unroll
    def "should generate validation violation on illegal destination account number #destinationAccount"() {
        given:
        def transfer = new Transfer(10001, destinationAccount, 4.5G)

        when:
        def violations = validator.validate(transfer)

        then:
        !violations.isEmpty()

        where:
        destinationAccount << [9999, 100000, null]
    }

    @Unroll
    def "should generate validation violation on illegal amount #amount"() {
        given:
        def transfer = new Transfer(10001, 10001, amount)

        when:
        def violations = validator.validate(transfer)

        then:
        !violations.isEmpty()

        where:
        amount << [0.009G, null, -1.5G]
    }
}
