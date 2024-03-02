package com.ug.orderservice.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ug.orderservice.model.entity.Step;
import com.ug.orderservice.model.entity.StepStatus;
import com.ug.orderservice.model.entity.Transaction;
import com.ug.orderservice.model.kafka.TransactionPacket;
import com.ug.orderservice.model.request.PlaceOrderRequest;
import com.ug.orderservice.repository.TransactionRepository;
import com.ug.orderservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {

    @Value("${kafka.topic.txn-step}")
    private String TXN_STEP_TOPIC;
    @Value("${kafka.message.reduce_quantity}")
    private String REDUCE_QUANTITY_MESSAGE;

    @Autowired private KafkaTemplate<String, String> kafkaTemplate;
    @Autowired TransactionRepository transactionRepository;

    @Transactional
    public void placeOrder(PlaceOrderRequest placeOrderRequest){

        final UUID txnId = UUID.randomUUID();
        ObjectMapper objectMapper = new ObjectMapper();

        transactionRepository.save(
                Transaction.builder()
                        ._id(txnId.toString())
                        .createdAt(new Date())
                        .steps(
                                List.of(
                                        Step.builder()
                                                .message(REDUCE_QUANTITY_MESSAGE)
                                                .status(StepStatus.INITIATED)
                                                .createdAt(new Date())
                                                .build()
                                ))
                        .build()
        );

        try {
            kafkaTemplate.send(
                    TXN_STEP_TOPIC,
                    objectMapper.writeValueAsString(
                            TransactionPacket.builder()
                                    .txnId(txnId)
                                    .message(REDUCE_QUANTITY_MESSAGE)
                                    .itemId(placeOrderRequest.getItemId())
                                    .quantity(placeOrderRequest.getQuantity())
                                    .build())
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }


}
