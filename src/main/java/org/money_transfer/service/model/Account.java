package org.money_transfer.service.model;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author Maksym Bondarenko
 * @version 1.0 12.03.19
 */
@Value
@NotNull
@Builder
public class Account {

    private String name;

    @NotNull
    @Min(10000)
    @Max(99999)
    private Long number;

    @NotNull
    @DecimalMin(value = "0.00")
    private BigDecimal balance;
}
