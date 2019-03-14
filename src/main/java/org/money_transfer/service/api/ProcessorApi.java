package org.money_transfer.service.api;

import org.money_transfer.service.service.QueueProcessor;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * @author Maksym Bondarenko
 * @version 1.0 14.03.19
 */
@Path("/processing")
public class ProcessorApi {

    private final QueueProcessor queueProcessor;

    @Inject
    public ProcessorApi(QueueProcessor queueProcessor) {
        this.queueProcessor = queueProcessor;
    }

    @GET
    @Path("/start")
    @Produces(MediaType.APPLICATION_JSON)
    public String startProcessing() {
        queueProcessor.start();
        return "OK";
    }

    @GET
    @Path("/stop")
    @Produces(MediaType.APPLICATION_JSON)
    public String stopProcessing() {
        queueProcessor.stop();
        return "OK";
    }
}
