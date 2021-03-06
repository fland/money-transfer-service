package org.money_transfer.service;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.glassfish.jersey.servlet.ServletContainer;
import org.glassfish.jersey.servlet.ServletProperties;
import org.money_transfer.service.config.JerseyResourceConfig;

import static org.eclipse.jetty.servlet.ServletContextHandler.NO_SESSIONS;

/**
 * @author Maksym Bondarenko
 * @version 1.0 12.03.19
 */
@Slf4j
class MoneyTransferService {

    private final int serverPort = Config.SERVER_PORT;

    Server start() {
        var maxThreads = 100;
        var minThreads = 10;
        var idleTimeout = 120;

        var threadPool = new QueuedThreadPool(maxThreads, minThreads, idleTimeout);
        var server = new Server(threadPool);
        var connector = new ServerConnector(server);
        connector.setPort(serverPort);
        server.setConnectors(new Connector[]{connector});

        var servletContextHandler = new ServletContextHandler(NO_SESSIONS);
        servletContextHandler.setContextPath("/");

        var servletHolder = new ServletHolder(ServletContainer.class);
        servletHolder.setInitOrder(0);
        servletHolder.setInitParameter(
                org.glassfish.jersey.server.ServerProperties.PROVIDER_PACKAGES,
                "org.money_transfer.service.api"
        );
        servletHolder.setInitParameter(
                ServletProperties.JAXRS_APPLICATION_CLASS,
                JerseyResourceConfig.class.getName());
        servletContextHandler.addServlet(servletHolder, "/api/*");

        server.setHandler(servletContextHandler);

        try {
            server.start();
        } catch (Exception e) {
            log.error("Exception: " + e, e);
            server.destroy();
            System.exit(1);
        }

        return server;
    }
}
