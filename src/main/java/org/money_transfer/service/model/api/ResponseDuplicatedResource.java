package org.money_transfer.service.model.api;

import lombok.NonNull;
import lombok.Value;

/**
 * @author Maksym Bondarenko
 * @version 1.0 14.03.19
 */
@Value
public class ResponseDuplicatedResource {

    private final String message = "Resource already exists";

    @NonNull
    private final String resourceName;
}
