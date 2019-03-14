package org.money_transfer.service.model.api;

import lombok.Value;

import java.math.BigDecimal;

/**
 * @author Maksym Bondarenko
 * @version 1.0 14.03.19
 */


@Value
public class ResponseNotSufficientFunds {

    private final String message = "Not sufficient funds on source account";

    private final BigDecimal amount;

}
