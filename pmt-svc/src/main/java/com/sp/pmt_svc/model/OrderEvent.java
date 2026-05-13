package com.sp.pmt_svc.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import tools.jackson.databind.json.JsonMapper;

public record OrderEvent (String orderId, String userId, double amount){
}
