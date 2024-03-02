package com.ug.orderservice.repository;

import com.ug.orderservice.model.entity.Step;
import com.ug.orderservice.model.entity.StepStatus;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface TransactionRepositoryCustom {
    @Transactional
    void addStep(UUID txnId, Step step);

    void updateStepStatus(UUID txnId, String message, StepStatus stepStatus);
}
