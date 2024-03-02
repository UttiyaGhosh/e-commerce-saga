package com.ug.orderservice.model;

import lombok.*;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionParameter {

    @NonNull UUID txnId;
    @NonNull String message;
    long itemId;
    int quantity;
}
