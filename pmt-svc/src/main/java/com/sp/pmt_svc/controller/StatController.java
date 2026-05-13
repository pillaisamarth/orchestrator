package com.sp.pmt_svc.controller;

import com.sp.pmt_svc.context.StatContext;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/stats")
public class StatController {

    @GetMapping("/success")
    public ResponseEntity<String> getSuccessfulOrderCount(){
        return ResponseEntity.ok("Number of orders successfully processed: %s\n".formatted(StatContext.numOrdersProcessedSuccessfully));
    }

    @GetMapping("/dlt")
    public ResponseEntity<String> getDeadLetterTopicOrderCount(){
        return ResponseEntity.ok("Number of orders send to dlt post retries: %s\n".formatted(StatContext.numOrdersMovedToDLT));
    }

    @GetMapping("/received")
    public ResponseEntity<String> getReceivedOrderCount(){
        return ResponseEntity.ok("Number of orders received: %s\n".formatted(StatContext.numOrdersReceived));
    }
}
