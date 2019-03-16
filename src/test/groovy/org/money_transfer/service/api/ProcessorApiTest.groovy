package org.money_transfer.service.api

import org.money_transfer.service.service.QueueProcessor
import spock.lang.Specification

/**
 * @author Maksym Bondarenko
 * @version 1.0 16.03.19
 */

class ProcessorApiTest extends Specification {

    def queueProcessor = Mock(QueueProcessor)

    def "should successfully start processing"() {
        given:
        def processorApi = new ProcessorApi(queueProcessor)

        when:
        def response = processorApi.startProcessing()

        then:
        1 * queueProcessor.start()

        and:
        response == 'OK'
    }

    def "should successfully stop processing"() {
        given:
        def processorApi = new ProcessorApi(queueProcessor)

        when:
        def response = processorApi.stopProcessing()

        then:
        1 * queueProcessor.stop()

        and:
        response == 'OK'
    }
}
