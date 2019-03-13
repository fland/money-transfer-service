package org.money_transfer.service.model.api;

import lombok.Value;

/**
 * @author Maksym Bondarenko
 * @version 1.0 13.03.19
 */
@Value
public final class ResponseNotFound {

    private final String message = "Requested resource not found";

    private final String resourceName;
}
