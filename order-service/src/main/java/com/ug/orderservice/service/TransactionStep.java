package com.ug.orderservice.service;

import com.ug.orderservice.model.TransactionParameter;

public interface TransactionStep {
    void register();
    void execute(TransactionParameter transactionParameter);
}
