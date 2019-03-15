package org.money_transfer.service.exception.handler

import org.money_transfer.service.exception.ApiRequestValidationException
import org.money_transfer.service.model.api.ResponseInvalidRequest
import spock.lang.Specification

import javax.validation.ConstraintViolation

import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE
import static javax.ws.rs.core.Response.Status.BAD_REQUEST

/**
 * @author Maksym Bondarenko
 * @version 1.0 15.03.19
 */

class ApiRequestValidationExceptionHandlerTest extends Specification {

    def "should throw IllegalArgumentException on null exception argument"() {
        when:
        new ApiRequestValidationExceptionHandler().toResponse(null)

        then:
        thrown IllegalArgumentException
    }

    def "should return Bad Request response"() {
        given:
        def handler = new ApiRequestValidationExceptionHandler()
        def violations = [Stub(ConstraintViolation), Stub(ConstraintViolation)] as Set

        when:
        def response = handler.toResponse(new ApiRequestValidationException(violations))

        then:
        response.getStatus() == BAD_REQUEST.statusCode
        response.getEntity() == new ResponseInvalidRequest(violations)
        response.getMediaType() == APPLICATION_JSON_TYPE
    }
}
