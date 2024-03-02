package com.ug.orderservice.model.entity;

import lombok.*;

import java.util.Date;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Step {
    @NonNull private String message;
    @NonNull private StepStatus stepStatus;
    private Date createdAt;
}
