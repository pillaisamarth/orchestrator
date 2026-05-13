package com.sp.pmt_svc.handlers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.kafka.autoconfigure.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ConsumerAwareListenerErrorHandler;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.BackOff;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
@Slf4j
public class KafkaErrorHandlerConfig {

    //this error handler is invoked when an error is thrown and not caught in listener. If not
    //caught this will propagate to listener container (kafkalistenercontainer). Even when we're
    //using concurrent kafka listener container, it'll just work as parent or master and the actual
    // kafka listener method has kafka listener container wrapped around. concurrency of 6 means
    // there will be 6 kafka listener container that has kafkaconsumer (performing polls) wrapped
    // within it, and this container will call the listener method (annotated with @kafkalistener)
    // after fetching the record
    @Bean
    public ConsumerAwareListenerErrorHandler customErrorHandler(){
        return (msg, ex, consumer) -> {
            log.error("Exception thrown in the listener while processing message", ex);
            throw ex;
        };
    }

    //this error handler is invoked when error is thrown to kafka listernr contianer
    //default dead-letter topic is original topic + "-dlt"
    @Bean
    public DefaultErrorHandler defaultErrorHandler(KafkaTemplate<Object, Object> template){
        DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(template);
        BackOff backOff = new FixedBackOff(3000L, 2L);

        return new DefaultErrorHandler(recoverer, backOff);
    }
}
