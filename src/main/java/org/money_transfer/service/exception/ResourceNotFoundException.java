package org.money_transfer.service.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Maksym Bondarenko
 * @version 1.0 13.03.19
 */
@Getter
@RequiredArgsConstructor
public final class ResourceNotFoundException extends RuntimeException {

    private final String resourceName;
}
