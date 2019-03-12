package org.money_transfer.service;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Maksym Bondarenko
 * @version 1.0 12.03.19
 */
@Slf4j
public class Main {


    public static void main(String[] args) {
        log.info("Hello world!");

        MoneyTransferService service = new MoneyTransferService();
        service.start();
    }
}
