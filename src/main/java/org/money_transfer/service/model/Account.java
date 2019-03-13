package org.money_transfer.service.model;

import lombok.Value;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author Maksym Bondarenko
 * @version 1.0 12.03.19
 */
@Value
public class Account {

    private String name;

    @NotNull
    @Min(10000)
    @Max(99999)
    private Long number;

    @NotNull
    private BigDecimal balance;
}
