package org.money_transfer.service.service;

import javax.inject.Singleton;
import java.util.UUID;

/**
 * @author Maksym Bondarenko
 * @version 1.0 16.03.19
 */
@Singleton
public class TransferUuidProvider {

    public String provideUuid() {
        return UUID.randomUUID().toString();
    }
}
