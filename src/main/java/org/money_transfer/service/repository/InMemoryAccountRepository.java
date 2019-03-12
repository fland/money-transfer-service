package org.money_transfer.service.repository;

import lombok.NonNull;
import org.money_transfer.service.model.Account;

import javax.inject.Singleton;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author Maksym Bondarenko
 * @version 1.0 12.03.19
 */
@Singleton
public class InMemoryAccountRepository implements AccountRepository {

    private final ConcurrentMap<Long, Account> accounts;

    public InMemoryAccountRepository() {
        this.accounts = new ConcurrentHashMap<>();
    }

    @Override
    public void putAccount(@NonNull Account account) {
        accounts.put(account.getNumber(), account);
    }

    @Override
    public Account getAccount(long accountNumber) {
        return accounts.get(accountNumber);
    }
}
