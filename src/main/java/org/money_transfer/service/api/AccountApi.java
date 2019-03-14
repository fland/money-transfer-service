package org.money_transfer.service.api;

import org.money_transfer.service.exception.ApiRequestValidationException;
import org.money_transfer.service.exception.ResourceNotFoundException;
import org.money_transfer.service.model.Account;
import org.money_transfer.service.repository.AccountRepository;

import javax.inject.Inject;
import javax.validation.Validator;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 * @author Maksym Bondarenko
 * @version 1.0 12.03.19
 */
@Path("/account")
public class AccountApi {

    private final AccountRepository accountRepository;
    private final Validator validator;

    @Inject
    public AccountApi(AccountRepository accountRepository, Validator validator) {
        this.accountRepository = accountRepository;
        this.validator = validator;
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response putAccount(Account account, @Context UriInfo uriInfo) {
        var violations = validator.validate(account);
        if (!violations.isEmpty()) {
            throw new ApiRequestValidationException(violations);
        }
        accountRepository.putAccount(account);
        return Response
                .created(uriInfo
                        .getAbsolutePathBuilder()
                        .path("/{accountNumber}")
                        .build(account.getNumber()))
                .build();
    }

    @GET
    @Path("/{accountNumber}")
    @Produces(MediaType.APPLICATION_JSON)
    public Account getAccount(@PathParam("accountNumber") Long accountNumber) {
        var account = accountRepository.getAccount(accountNumber);
        if (account == null) {
            throw new ResourceNotFoundException(accountNumber.toString());
        }
        return account;
    }
}
