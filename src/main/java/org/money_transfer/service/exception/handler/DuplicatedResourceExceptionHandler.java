package org.money_transfer.service.exception.handler;

import org.money_transfer.service.exception.DuplicatedResourceException;
import org.money_transfer.service.model.api.ResponseDuplicatedResource;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

/**
 * @author Maksym Bondarenko
 * @version 1.0 14.03.19
 */

public class DuplicatedResourceExceptionHandler implements ExceptionMapper<DuplicatedResourceException> {

    @Override
    public Response toResponse(DuplicatedResourceException e) {
        return Response
                .status(BAD_REQUEST)
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .entity(new ResponseDuplicatedResource(e.getResourceName()))
                .build();
    }
}
