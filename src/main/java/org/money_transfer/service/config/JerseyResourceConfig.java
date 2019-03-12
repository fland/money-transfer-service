package org.money_transfer.service.config;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.money_transfer.service.repository.AccountRepository;
import org.money_transfer.service.repository.InMemoryAccountRepository;

import javax.inject.Singleton;
import javax.ws.rs.ApplicationPath;

/**
 * @author Maksym Bondarenko
 * @version 1.0 12.03.19
 */
@ApplicationPath("/api")
public class JerseyResourceConfig extends ResourceConfig {

    public JerseyResourceConfig() {
        register(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(InMemoryAccountRepository.class)
                        .to(AccountRepository.class)
                        .in(Singleton.class);
            }
        });
    }
}
