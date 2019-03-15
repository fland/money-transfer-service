package org.money_transfer.service.model.api

import spock.lang.Specification

/**
 * @author Maksym Bondarenko
 * @version 1.0 15.03.19
 */

class ResponseNotFoundTest extends Specification {

    def "should throw IllegalArgumentException on null resource name"() {
        when:
        new ResponseNotFound(null)

        then:
        thrown IllegalArgumentException
    }
}
