package com.ug.orderservice.model.kafka;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReduceQuantityRequest {
    UUID txnId;
    String message;
    long itemId;
    int quantity;
}
