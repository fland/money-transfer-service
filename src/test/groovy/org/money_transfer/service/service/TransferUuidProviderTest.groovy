package org.money_transfer.service.service

import spock.lang.Specification

/**
 * @author Maksym Bondarenko
 * @version 1.0 16.03.19
 */

class TransferUuidProviderTest extends Specification {

    def "should provide uuid in expected format"() {
        expect:
        new TransferUuidProvider().provideUuid().matches('[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}')
    }
}
