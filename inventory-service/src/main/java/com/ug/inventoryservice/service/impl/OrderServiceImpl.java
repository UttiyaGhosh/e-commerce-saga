package com.ug.inventoryservice.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ug.inventoryservice.model.kafka.ReduceQuantityRequest;
import com.ug.inventoryservice.service.InventoryService;
import com.ug.inventoryservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderServiceImpl implements OrderService {

    @Value("${kafka.topic.txn-step}")
    private String TXN_STEP_TOPIC;

    @Value("${kafka.message.reduce_quantity}")
    private String REDUCE_QUANTITY_MESSAGE;

    @Autowired private InventoryService inventoryService;
    @KafkaListener(topics="${kafka.topic.txn-step}", groupId="my_group_id")
    @Transactional
    public void initiateStep(String txnObjectString){

        ObjectMapper objectMapper = new ObjectMapper();

        try {
                final ReduceQuantityRequest reduceQuantityRequest = objectMapper.readValue(
                        txnObjectString, ReduceQuantityRequest.class);
                if(reduceQuantityRequest.getMessage().equals(REDUCE_QUANTITY_MESSAGE))
                    inventoryService.reduceQuantity(
                            reduceQuantityRequest.getTxnId(),
                            reduceQuantityRequest.getItemId(),
                            reduceQuantityRequest.getQuantity());
            }catch (JsonProcessingException e) {
                throw new RuntimeException(e);
        }

    }
}
