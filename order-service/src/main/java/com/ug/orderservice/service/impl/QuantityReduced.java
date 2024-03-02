package com.ug.orderservice.service.impl;

import com.ug.orderservice.model.TransactionParameter;
import com.ug.orderservice.service.TransactionStep;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;

public class QuantityReduced implements TransactionStep {

    @Value("${kafka.message.quantity_reduced}")
    private String QUANTITY_REDUCED_MESSAGE;
    @Override
    @PostConstruct
    public void register() {
        TransactionServiceImpl.addStep(QUANTITY_REDUCED_MESSAGE,this);
    }

    @Override
    public void execute(TransactionParameter transactionParameter) {

    }
}
