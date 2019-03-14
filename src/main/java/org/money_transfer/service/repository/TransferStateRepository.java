package org.money_transfer.service.repository;

import org.money_transfer.service.model.TransferState;

import java.util.Set;

/**
 * @author Maksym Bondarenko
 * @version 1.0 14.03.19
 */

public interface TransferStateRepository {

    void putState(TransferState transferState);

    TransferState getState(String transferUuid);

    Set<TransferState> getStatesByAccount(long accountNumber);
}
