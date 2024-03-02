package com.ug.inventoryservice.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ug.inventoryservice.model.kafka.TransactionResponse;
import com.ug.inventoryservice.repository.InventoryRepository;
import com.ug.inventoryservice.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class InventoryServiceImpl implements InventoryService {

    @Value("${kafka.topic.txn-step}")
    private String TXN_STEP_TOPIC;

    @Value("${kafka.message.quantity_reduced}")
    private String QUANTITY_REDUCED_MESSAGE;
    @Value("${kafka.message.insufficient_quantity}")
    private String INSUFFICIENT_QUANTITY_MESSAGE;

    @Autowired private KafkaTemplate<String, String> kafkaTemplate;
    @Autowired private InventoryRepository inventoryRepository;

    @Transactional
    public void reduceQuantity(UUID txnId, Long itemId, int quantity){

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            if(inventoryRepository.reduceQuantity(itemId,quantity)==1) {
                kafkaTemplate.send(
                        TXN_STEP_TOPIC,
                        objectMapper.writeValueAsString(
                                TransactionResponse.builder()
                                        .txnId(txnId)
                                        .message(QUANTITY_REDUCED_MESSAGE)
                                        .build())
                );
            }else {
                kafkaTemplate.send(
                        TXN_STEP_TOPIC,
                        objectMapper.writeValueAsString(
                                TransactionResponse.builder()
                                        .txnId(txnId)
                                        .message(INSUFFICIENT_QUANTITY_MESSAGE)
                                        .build())
                );
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
