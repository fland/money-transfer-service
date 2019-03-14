package org.money_transfer.service.model;

import lombok.Value;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author Maksym Bondarenko
 * @version 1.0 14.03.19
 */
@Value
@NotNull
public class Transfer {

    @NotNull
    @Min(10000)
    @Max(99999)
    private final Long sourceAccount;

    @NotNull
    @Min(10000)
    @Max(99999)
    private final Long destinationAccount;

    @NotNull
    @DecimalMin(value = "0.01")
    private final BigDecimal amount;
}
