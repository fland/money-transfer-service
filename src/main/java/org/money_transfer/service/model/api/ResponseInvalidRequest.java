package org.money_transfer.service.model.api;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import javax.validation.ConstraintViolation;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Maksym Bondarenko
 * @version 1.0 13.03.19
 */
@Getter
@ToString
@EqualsAndHashCode
public final class ResponseInvalidRequest {

    private final String message = "Invalid request";

    private final Map<String, String> invalidParameters;

    public ResponseInvalidRequest(@NonNull Set<? extends ConstraintViolation<?>> violations) {
        invalidParameters = new HashMap<>();
        violations.forEach(i -> invalidParameters.put(i.getPropertyPath().toString(), i.getMessage()));
    }
}
