package org.money_transfer.service.exception;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * @author Maksym Bondarenko
 * @version 1.0 14.03.19
 */
@Getter
@RequiredArgsConstructor
public final class DuplicatedResourceException extends RuntimeException {

    @NonNull
    private final String resourceName;
}
