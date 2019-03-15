package org.money_transfer.service.exception.handler

import org.money_transfer.service.exception.DuplicatedResourceException
import org.money_transfer.service.model.api.ResponseDuplicatedResource
import spock.lang.Specification

import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE
import static javax.ws.rs.core.Response.Status.BAD_REQUEST

/**
 * @author Maksym Bondarenko
 * @version 1.0 15.03.19
 */

class DuplicatedResourceExceptionHandlerTest extends Specification {

    def "should throw IllegalArgumentException on null exception argument"() {
        when:
        new DuplicatedResourceExceptionHandler().toResponse(null)

        then:
        thrown IllegalArgumentException
    }

    def "should return Bad Request response"() {
        given:
        def handler = new DuplicatedResourceExceptionHandler()
        def resourceName = 'resource123'

        when:
        def response = handler.toResponse(new DuplicatedResourceException(resourceName))

        then:
        response.getStatus() == BAD_REQUEST.statusCode
        response.getEntity() == new ResponseDuplicatedResource(resourceName)
        response.getMediaType() == APPLICATION_JSON_TYPE
    }
}
