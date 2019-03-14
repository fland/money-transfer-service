package org.money_transfer.service.service;

import lombok.extern.slf4j.Slf4j;
import org.money_transfer.service.model.Account;
import org.money_transfer.service.model.TransferState;
import org.money_transfer.service.repository.AccountRepository;
import org.money_transfer.service.repository.TransferStateRepository;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Maksym Bondarenko
 * @version 1.0 14.03.19
 */
@Singleton
@Slf4j
public class QueueProcessor {

    private final ExecutorService executorService;
    private final AccountRepository accountRepository;
    private final TransferQueueService transferQueueService;
    private final TransferStateRepository transferStateRepository;
    private final AtomicBoolean running;

    @Inject
    public QueueProcessor(AccountRepository accountRepository, TransferQueueService transferQueueService,
                          TransferStateRepository transferStateRepository) {
        this.accountRepository = accountRepository;
        this.transferQueueService = transferQueueService;
        this.transferStateRepository = transferStateRepository;
        this.running = new AtomicBoolean(false);
        executorService = Executors.newSingleThreadExecutor();
    }

    public void start() {
        running.set(true);
        executorService.submit(() -> {
            while (running.get()) {
                process();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void process() {
        transferQueueService.get().ifPresent(transferUuid -> {
            var transferState = transferStateRepository.getState(transferUuid);
            log.info("Processing transfer: {}", transferState);
            if (transferState != null && transferState.getState().equals(TransferState.State.ACCEPTED)) {
                var sourceAccount = accountRepository.getAccount(transferState.getSourceAccount());
                if (sourceAccount == null) {
                    log.warn("No source account {} while processing state {}, marking failed",
                            transferState.getSourceAccount(), transferState.getUuid());
                    putFailedState(transferState);
                    return;
                }
                var destinationAccount = accountRepository.getAccount(transferState.getDestinationAccount());
                if (destinationAccount == null) {
                    log.warn("No destination account {} while processing state {}, marking failed",
                            transferState.getSourceAccount(), transferState.getUuid());
                    putFailedState(transferState);
                    return;
                }
                BigDecimal adoptedAmount = transferState.getAmount().setScale(2, RoundingMode.DOWN);
                if (sourceAccount.getBalance().compareTo(adoptedAmount) <= -1) {
                    log.warn("Not sufficient funds {} on account {} while processing state {} with amount {}, marking failed",
                            sourceAccount.getBalance(), transferState.getSourceAccount(), transferState.getUuid(), transferState.getAmount());
                    putFailedState(transferState);
                    return;
                }

                accountRepository.putAccount(Account
                        .builder()
                        .name(sourceAccount.getName())
                        .number(sourceAccount.getNumber())
                        .balance(sourceAccount.getBalance().subtract(adoptedAmount))
                        .build());
                accountRepository.putAccount(Account
                        .builder()
                        .name(destinationAccount.getName())
                        .number(destinationAccount.getNumber())
                        .balance(destinationAccount.getBalance().add(adoptedAmount))
                        .build());
                transferStateRepository.putState(TransferState
                        .builder()
                        .uuid(transferState.getUuid())
                        .amount(transferState.getAmount())
                        .sourceAccount(transferState.getSourceAccount())
                        .destinationAccount(transferState.getDestinationAccount())
                        .state(TransferState.State.PROCESSED)
                        .build());
                log.info("Transfer {} processed", transferState.getUuid());
            }
        });
    }

    private void putFailedState(TransferState transferState) {
        transferStateRepository.putState(TransferState
                .builder()
                .uuid(transferState.getUuid())
                .sourceAccount(transferState.getSourceAccount())
                .destinationAccount(transferState.getDestinationAccount())
                .amount(transferState.getAmount())
                .state(TransferState.State.FAILED)
                .build());
    }

    public void stop() {
        running.set(false);
    }
}
