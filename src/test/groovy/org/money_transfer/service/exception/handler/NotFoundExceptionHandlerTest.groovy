package org.money_transfer.service.exception.handler


import org.money_transfer.service.exception.ResourceNotFoundException
import org.money_transfer.service.model.api.ResponseNotFound
import spock.lang.Specification

import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE
import static javax.ws.rs.core.Response.Status.NOT_FOUND

/**
 * @author Maksym Bondarenko
 * @version 1.0 15.03.19
 */

class NotFoundExceptionHandlerTest extends Specification {

    def "should throw IllegalArgumentException on null exception argument"() {
        when:
        new NotFoundExceptionHandler().toResponse(null)

        then:
        thrown IllegalArgumentException
    }

    def "should return Bad Request response"() {
        given:
        def handler = new NotFoundExceptionHandler()
        def resourceName = 'resource123'

        when:
        def response = handler.toResponse(new ResourceNotFoundException(resourceName))

        then:
        response.getStatus() == NOT_FOUND.statusCode
        response.getEntity() == new ResponseNotFound(resourceName)
        response.getMediaType() == APPLICATION_JSON_TYPE
    }
}
