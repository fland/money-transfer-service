package org.money_transfer.service.api;

import org.money_transfer.service.model.Account;
import org.money_transfer.service.repository.AccountRepository;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * @author Maksym Bondarenko
 * @version 1.0 12.03.19
 */
@Path("/account")
public class AccountApi {

    private final AccountRepository accountRepository;

    @Inject
    public AccountApi(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @PUT
    @Produces(MediaType.TEXT_PLAIN)
    public String putAccount(Account account) {
        accountRepository.putAccount(account);
        return "ok";
    }

    @GET
    @Path("/{accountNumber}")
    @Produces(MediaType.APPLICATION_JSON)
    public Account getAccount(@PathParam("accountNumber") Long accountNumber) {
        return accountRepository.getAccount(accountNumber);
    }
}
