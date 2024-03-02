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
public class TransactionPacket {
    UUID txnId;
    String message;
    long itemId;
    double price;
    int quantity;
}
