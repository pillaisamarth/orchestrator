package com.sp.pmt_svc.kafka;

import com.sp.pmt_svc.context.StatContext;
import com.sp.pmt_svc.model.OrderEvent;
import com.sp.pmt_svc.service.IdempotentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderListener {
    private final IdempotentService idempotentService;
    private final JsonMapper jsonMapper;

    @KafkaListener(topics = "order-placed", errorHandler = "customErrorHandler", concurrency = "3")
    public void listenOrderPlaced(String message){
        StatContext.numOrdersReceived.incrementAndGet();
        log.info("Received OrderPlaced event: {}", message);

        if(Math.random() < 0.3){
            log.error("Simulated failure - throwing exception");
            throw new RuntimeException("Payment processing failed");
        }

        OrderEvent event = jsonMapper.readValue(message, OrderEvent.class);
        String orderId = event.orderId();

        Boolean alreadyProcessed = idempotentService.isDuplicateAndStore(orderId);

        if(alreadyProcessed){
            StatContext.numDuplicateOrder.incrementAndGet();
            log.info("Skipping duplicate orderId: {}", orderId);
            return;
        }
        StatContext.numOrdersProcessedSuccessfully.incrementAndGet();
        log.info("Successfully processed order with id:{}", orderId);
    }
}
