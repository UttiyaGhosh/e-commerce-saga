package com.ug.orderservice.service.impl;

import com.ug.orderservice.model.TransactionParameter;
import com.ug.orderservice.service.TransactionStep;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;

public class InsufficientQuantity implements TransactionStep {

    @Value("${kafka.message.insufficient_quantity}")
    private String INSUFFICIENT_QUANTITY_MESSAGE;
    @Override
    @PostConstruct
    public void register() {
        TransactionServiceImpl.addStep(INSUFFICIENT_QUANTITY_MESSAGE,this);
    }

    @Override
    public void execute(TransactionParameter transactionParameter) {

    }
}
