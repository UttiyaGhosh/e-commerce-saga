package com.ug.inventoryservice.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

public interface OrderService {

    public void initiateStep(String txnObject);
}
