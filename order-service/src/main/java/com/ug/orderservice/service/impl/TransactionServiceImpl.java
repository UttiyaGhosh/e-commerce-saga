package com.ug.orderservice.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ug.orderservice.model.kafka.TransactionPacket;
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
    public void listenToSteps(String txnResponseString) {

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            final TransactionPacket transactionPacket = objectMapper.readValue(
                    txnResponseString, TransactionPacket.class);
            Optional.ofNullable(transactionStepMap.get(transactionPacket.getMessage()))
                    .ifPresent(transactionStep -> transactionStep.execute(transactionPacket));
        }catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}