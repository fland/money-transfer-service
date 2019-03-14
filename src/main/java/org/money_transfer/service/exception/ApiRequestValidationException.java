package org.money_transfer.service.exception;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintViolation;
import java.util.Set;

/**
 * @author Maksym Bondarenko
 * @version 1.0 13.03.19
 */
@Getter
@RequiredArgsConstructor
public final class ApiRequestValidationException extends RuntimeException {

    @NonNull
    private final Set<? extends ConstraintViolation<?>> violations;
}
