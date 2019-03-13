package org.money_transfer.service.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.money_transfer.service.model.api.ResponseInvalidRequest;

/**
 * @author Maksym Bondarenko
 * @version 1.0 13.03.19
 */
@Getter
@RequiredArgsConstructor
public final class ApiRequestValidationException extends RuntimeException {

    private final ResponseInvalidRequest apiResponse;

}
