package org.money_transfer.service.api;

import org.money_transfer.service.exception.ApiRequestValidationException;
import org.money_transfer.service.exception.ResourceNotFoundException;
import org.money_transfer.service.model.Transfer;
import org.money_transfer.service.model.TransferState;
import org.money_transfer.service.repository.AccountRepository;
import org.money_transfer.service.repository.TransferStateRepository;
import org.money_transfer.service.service.TransferQueueService;
import org.money_transfer.service.service.TransferUuidProvider;

import javax.inject.Inject;
import javax.validation.Validator;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Set;

/**
 * @author Maksym Bondarenko
 * @version 1.0 14.03.19
 */
@Path("/transfer")
public class TransferApi {

    private final AccountRepository accountRepository;
    private final TransferQueueService transferQueueService;
    private final TransferStateRepository transferStateRepository;
    private final Validator validator;
    private final TransferUuidProvider transferUuidProvider;

    @Inject
    TransferApi(AccountRepository accountRepository, TransferQueueService transferQueueService,
                TransferStateRepository transferStateRepository, Validator validator, TransferUuidProvider transferUuidProvider) {
        this.accountRepository = accountRepository;
        this.transferQueueService = transferQueueService;
        this.transferStateRepository = transferStateRepository;
        this.validator = validator;
        this.transferUuidProvider = transferUuidProvider;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response postTransfer(Transfer transfer) {
        var violations = validator.validate(transfer);
        if (!violations.isEmpty()) {
            throw new ApiRequestValidationException(violations);
        }
        var sourceAccount = accountRepository.getAccount(transfer.getSourceAccount());
        if (sourceAccount == null) {
            throw new ResourceNotFoundException(transfer.getSourceAccount().toString());
        }
        var destinationAccount = accountRepository.getAccount(transfer.getDestinationAccount());
        if (destinationAccount == null) {
            throw new ResourceNotFoundException(transfer.getDestinationAccount().toString());
        }
        var transferState = TransferState
                .builder()
                .state(TransferState.State.ACCEPTED)
                .sourceAccount(transfer.getSourceAccount())
                .destinationAccount(transfer.getDestinationAccount())
                .amount(transfer.getAmount())
                .uuid(transferUuidProvider.provideUuid())
                .build();
        transferStateRepository.putState(transferState);
        transferQueueService.put(transferState.getUuid());

        return Response
                .accepted()
                .build();
    }

    @GET
    @Path("/account/{accountNumber}")
    @Produces(MediaType.APPLICATION_JSON)
    public Set<TransferState> getTransfersByAccount(@PathParam("accountNumber") Long accountNumber) {
        return transferStateRepository.getStatesByAccount(accountNumber);
    }
}
