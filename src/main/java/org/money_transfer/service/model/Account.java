package org.money_transfer.service.model;

import lombok.Value;

import java.math.BigDecimal;

/**
 * @author Maksym Bondarenko
 * @version 1.0 12.03.19
 */
@Value
public class Account {

    private String name;
    private Long number;
    private BigDecimal balance;
}
