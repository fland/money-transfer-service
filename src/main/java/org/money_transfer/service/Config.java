package org.money_transfer.service;

import lombok.experimental.UtilityClass;

import java.util.Optional;

/**
 * @author Maksym Bondarenko
 * @version 1.0 12.03.19
 */
@UtilityClass
public class Config {

    public final int SERVER_PORT = getIntEnv("SERVER_PORT").orElse(8080);

    private Optional<String> getEnv(String name) {
        return Optional.ofNullable(System.getenv(name));
    }

    private Optional<Integer> getIntEnv(String name) {
        return Optional.ofNullable(System.getenv(name)).map(Integer::parseInt);
    }
}
