package org.money_transfer.service.config;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.money_transfer.service.exception.handler.ApiRequestValidationExceptionHandler;
import org.money_transfer.service.exception.handler.DuplicatedResourceExceptionHandler;
import org.money_transfer.service.exception.handler.NotFoundExceptionHandler;
import org.money_transfer.service.repository.AccountRepository;
import org.money_transfer.service.repository.InMemoryAccountRepository;
import org.money_transfer.service.repository.InMemoryTransferStateRepository;
import org.money_transfer.service.repository.TransferStateRepository;
import org.money_transfer.service.service.QueueProcessor;
import org.money_transfer.service.service.TransferQueueService;
import org.money_transfer.service.service.TransferUuidProvider;

import javax.inject.Singleton;
import javax.validation.Validation;
import javax.validation.Validator;
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
                bind(InMemoryTransferStateRepository.class)
                        .to(TransferStateRepository.class)
                        .in(Singleton.class);
                bind(Validation.buildDefaultValidatorFactory().getValidator())
                        .to(Validator.class);
                bind(QueueProcessor.class)
                        .to(QueueProcessor.class)
                        .in(Singleton.class);
                bind(TransferQueueService.class)
                        .to(TransferQueueService.class)
                        .in(Singleton.class);
                bind(TransferUuidProvider.class)
                        .to(TransferUuidProvider.class)
                        .in(Singleton.class);
            }
        });
        register(new ApiRequestValidationExceptionHandler());
        register(new NotFoundExceptionHandler());
        register(new DuplicatedResourceExceptionHandler());
        register(new JacksonFeature());
    }
}
