package org.money_transfer.service.model;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.math.BigDecimal;

/**
 * @author Maksym Bondarenko
 * @version 1.0 14.03.19
 */
@Value
@Builder
public class TransferState {

    @NonNull
    private String uuid;

    @NonNull
    private Long sourceAccount;

    @NonNull
    private Long destinationAccount;

    @NonNull
    private BigDecimal amount;

    @NonNull
    private State state;

    public enum State {
        ACCEPTED, PROCESSED, FAILED
    }
}
