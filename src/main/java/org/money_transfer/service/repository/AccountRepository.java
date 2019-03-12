package org.money_transfer.service.repository;

import org.money_transfer.service.model.Account;

/**
 * @author Maksym Bondarenko
 * @version 1.0 12.03.19
 */
public interface AccountRepository {

    void putAccount(Account account);

    Account getAccount(long accountNumber);
}
