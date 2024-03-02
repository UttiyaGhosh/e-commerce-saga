package com.ug.orderservice.service;

import com.ug.orderservice.model.kafka.TransactionPacket;

public interface TransactionStep {
    void register();
    void execute(TransactionPacket transactionParameter);
}
