package com.ug.orderservice.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ug.orderservice.model.TransactionParameter;
import com.ug.orderservice.service.OrderService;
import com.ug.orderservice.service.TransactionStep;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Optional;

@Service
public class TransactionServiceImpl {

    private static HashMap<String, TransactionStep> transactionStepMap = new HashMap<>();

    public static void addStep(String message, TransactionStep transactionStep) {
        transactionStepMap.put(message, transactionStep);
    }

    @KafkaListener(topics = "${kafka.topic.txn-step}", groupId = "${kafka.group.id}")
    @Transactional
    public void listenToSteps(String txnParameterString) {

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            final TransactionParameter transactionParameter = objectMapper.readValue(
                    txnParameterString, TransactionParameter.class);
            Optional.ofNullable(transactionStepMap.get(transactionParameter.getMessage()))
                    .ifPresent(transactionStep -> transactionStep.execute(transactionParameter));
        }catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}