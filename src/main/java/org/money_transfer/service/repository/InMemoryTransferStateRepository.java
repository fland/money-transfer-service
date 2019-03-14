package org.money_transfer.service.repository;

import lombok.NonNull;
import org.money_transfer.service.model.TransferState;

import javax.inject.Singleton;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

/**
 * @author Maksym Bondarenko
 * @version 1.0 14.03.19
 */
@Singleton
public class InMemoryTransferStateRepository implements TransferStateRepository {

    private final ConcurrentMap<String, TransferState> states;

    public InMemoryTransferStateRepository() {
        states = new ConcurrentHashMap<>();
    }

    @Override
    public void putState(@NonNull TransferState transferState) {
        states.put(transferState.getUuid(), transferState);
    }

    @Override
    public TransferState getState(@NonNull String transferUuid) {
        return states.get(transferUuid);
    }

    @Override
    public Set<TransferState> getStatesByAccount(long accountNumber) {
        return states.values()
                .stream()
                .filter(i -> i.getSourceAccount() == accountNumber || i.getDestinationAccount() == accountNumber)
                .collect(Collectors.toSet());
    }
}
