package com.ug.orderservice.repository.impl;

import com.ug.orderservice.model.entity.Step;
import com.ug.orderservice.model.entity.StepStatus;
import com.ug.orderservice.model.entity.Transaction;
import com.ug.orderservice.repository.TransactionRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public class TransactionRepositoryCustomImpl implements TransactionRepositoryCustom {
    private static final String TRANSACTION_ID_FIELD = "_id";
    private static final String STEPS_FIELD = "steps";
    private static final String STEP_MESSAGE_FIELD = "message";
    @Autowired private MongoTemplate mongoTemplate;

    @Override
    @Transactional
    public void addStep(UUID txnId, Step step) {
        Query query = new Query(Criteria.where(TRANSACTION_ID_FIELD).is(txnId.toString()));

        Update update = new Update().push(STEPS_FIELD, step);

        mongoTemplate.updateFirst(query, update, Transaction.class);
    }
    @Override
    @Transactional
    public void updateStepStatus(UUID txnId, String message, StepStatus stepStatus) {
        Query query = new Query(
                        Criteria.where(TRANSACTION_ID_FIELD).is(txnId.toString()).andOperator(
                        Criteria.where(STEP_MESSAGE_FIELD).is(message)
        ));

        Update update = new Update().set("steps.$.status", stepStatus);

        mongoTemplate.updateFirst(query, update, Transaction.class);
    }
}
