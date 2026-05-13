package com.sp.pmt_svc.context;

import org.springframework.context.annotation.Bean;

import java.util.concurrent.atomic.AtomicLong;

public class StatContext {
    public static AtomicLong numOrdersReceived = new AtomicLong(0L);
    public static AtomicLong numOrdersProcessedSuccessfully = new AtomicLong(0L);
    public static AtomicLong numOrdersMovedToDLT = new AtomicLong(0L);
    public static AtomicLong numDuplicateOrder = new AtomicLong(0L);
}
