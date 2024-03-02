package com.ug.orderservice.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ug.orderservice.model.entity.Step;
import com.ug.orderservice.model.entity.StepStatus;
import com.ug.orderservice.model.entity.Transaction;
import com.ug.orderservice.model.kafka.TransactionPacket;
import com.ug.orderservice.repository.TransactionRepository;
import com.ug.orderservice.service.TransactionStep;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

public class QuantityReduced implements TransactionStep {

    @Value("${kafka.topic.txn-step}")
    private String TXN_STEP_TOPIC;
    @Value("${kafka.message.quantity_reduced}")
    private String QUANTITY_REDUCED_MESSAGE;
    @Value("${kafka.message.reduce_price}")
    private String REDUCE_PRICE_MESSAGE;
    @Value("${kafka.message.reduce_quantity_rollback}")
    private String REDUCE_QUANTITY_ROLLBACK;

    @Autowired private KafkaTemplate<String, String> kafkaTemplate;
    @Autowired TransactionRepository transactionRepository;
    @Override
    @PostConstruct
    public void register() {
        TransactionServiceImpl.addStep(QUANTITY_REDUCED_MESSAGE,this);
    }
    @Override
    @Transactional
    public void execute(TransactionPacket transactionPacket) {

        ObjectMapper objectMapper = new ObjectMapper();
        
        Transaction transaction = transactionRepository.findById(transactionPacket.getTxnId().toString())
                .orElseThrow(()->new RuntimeException("Transaction Not Found"));
        final Optional<Step> stepOptional = transaction.getSteps().stream()
                .filter(step -> step.getMessage().equals(QUANTITY_REDUCED_MESSAGE))
                .findFirst();

        if(stepOptional.isPresent()) {
            final Step step = stepOptional.get();
            if (step.getStatus().equals(StepStatus.INITIATED)) {

                transactionRepository.updateStepStatus(
                        transactionPacket.getTxnId(),
                        QUANTITY_REDUCED_MESSAGE,
                        StepStatus.COMPLETED);
                //Add reduce price to DB
                transactionRepository.addStep(
                        transactionPacket.getTxnId(),
                        Step.builder()
                                .message(REDUCE_PRICE_MESSAGE)
                                .status(StepStatus.INITIATED)
                                .createdAt(new Date())
                                .build()
                );
                //Push reduce price
                try {
                    kafkaTemplate.send(
                            TXN_STEP_TOPIC,
                            objectMapper.writeValueAsString(
                                    TransactionPacket.builder()
                                            .txnId(transactionPacket.getTxnId())
                                            .message(REDUCE_PRICE_MESSAGE)
                                            .itemId(transactionPacket.getItemId())
                                            .price(transactionPacket.getPrice())
                                            .build())
                    );
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            } else {
                //Initiate reduce quantity rollback
                try {
                    kafkaTemplate.send(
                            TXN_STEP_TOPIC,
                            objectMapper.writeValueAsString(
                                    TransactionPacket.builder()
                                            .txnId(transactionPacket.getTxnId())
                                            .message(REDUCE_QUANTITY_ROLLBACK)
                                            .itemId(transactionPacket.getItemId())
                                            .quantity(transactionPacket.getQuantity())
                                            .build())
                    );
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
