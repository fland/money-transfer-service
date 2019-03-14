package org.money_transfer.service.model

import spock.lang.Specification
import spock.lang.Unroll

/**
 * @author Maksym Bondarenko
 * @version 1.0 14.03.19
 */

class TransferStateTest extends Specification {

    @Unroll
    def "should throw IllegalArgumentException on null #argumentName"() {
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
        uuid    | sourceAccount | destinationAccount | amount | state                        | argumentName
        null    | 123           | 321                | 12.3G  | TransferState.State.ACCEPTED | 'uuid'
        'uuid1' | null          | 321                | 12.3G  | TransferState.State.ACCEPTED | 'sourceAccount'
        'uuid1' | 123           | null               | 12.3G  | TransferState.State.ACCEPTED | 'destinationAccount'
        'uuid1' | 123           | 321                | null   | TransferState.State.ACCEPTED | 'amount'
        'uuid1' | 123           | 321                | 74.3G  | null                         | 'state'
    }
}
