package org.money_transfer.service.service;

import lombok.NonNull;

import javax.inject.Singleton;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author Maksym Bondarenko
 * @version 1.0 14.03.19
 */
@Singleton
public class TransferQueueService {

    private final ConcurrentLinkedQueue<String> transactions;

    public TransferQueueService() {
        transactions = new ConcurrentLinkedQueue<>();
    }

    Optional<String> get() {
        return Optional.ofNullable(transactions.poll());
    }

    public boolean put(@NonNull String transactionUuid) {
        return transactions.add(transactionUuid);
    }
}
