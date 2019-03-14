package org.money_transfer.service.model

import spock.lang.Specification

/**
 * @author Maksym Bondarenko
 * @version 1.0 14.03.19
 */

class TransferStateTest extends Specification {

    def "should throw IllegalArgumentException on null values"() {
        when:
        TransferState
                .builder()
                .uuid(uuid)
                .sourceAccount(sourceAccount)
                .destinationAccount(destinationAccount)
                .amount(amount)
                .state(state)
                .build()

        then:
        thrown IllegalArgumentException

        where:
        uuid | sourceAccount | destinationAccount | amount | state
        null | 123           | 321                | 12.3G  | TransferState.State.ACCEPTED
    }
}
