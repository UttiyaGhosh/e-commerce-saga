package com.ug.orderservice.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ug.orderservice.model.kafka.ReduceQuantityRequest;
import com.ug.orderservice.model.request.PlaceOrderRequest;
import com.ug.orderservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {

    @Value("${kafka.topic.txn-step}")
    private String TXN_STEP_TOPIC;

    @Value("${kafka.message.reduce_quantity}")
    private String REDUCE_QUANTITY_MESSAGE;

    @Autowired private KafkaTemplate<String, String> kafkaTemplate;
    @Transactional
    public void placeOrder(PlaceOrderRequest placeOrderRequest){

        final UUID txnId = UUID.randomUUID();
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            kafkaTemplate.send(
                    TXN_STEP_TOPIC,
                objectMapper.writeValueAsString(
                        ReduceQuantityRequest.builder()
                                .txnId(txnId)
                                .message(REDUCE_QUANTITY_MESSAGE)
                                .itemId(placeOrderRequest.getItemId())
                                .quantity(placeOrderRequest.getQuantity())
                                .build()));
        }catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }
}
