package org.money_transfer.service.exception.handler;


import org.money_transfer.service.exception.ApiRequestValidationException;
import org.money_transfer.service.model.api.ResponseInvalidRequest;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * @author Maksym Bondarenko
 * @version 1.0 13.03.19
 */
public class ApiRequestValidationExceptionHandler implements ExceptionMapper<ApiRequestValidationException> {

    @Override
    public Response toResponse(ApiRequestValidationException e) {
        return Response
                .status(Response.Status.BAD_REQUEST)
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .entity(new ResponseInvalidRequest(e.getViolations()))
                .build();
    }
}
