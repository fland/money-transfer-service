package org.money_transfer.service.exception.handler;

import lombok.NonNull;
import org.money_transfer.service.exception.ResourceNotFoundException;
import org.money_transfer.service.model.api.ResponseNotFound;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;

/**
 * @author Maksym Bondarenko
 * @version 1.0 13.03.19
 */

public class NotFoundExceptionHandler implements ExceptionMapper<ResourceNotFoundException> {

    @Override
    public Response toResponse(@NonNull ResourceNotFoundException e) {
        return Response
                .status(NOT_FOUND)
                .header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON)
                .entity(new ResponseNotFound(e.getResourceName()))
                .build();
    }
}
