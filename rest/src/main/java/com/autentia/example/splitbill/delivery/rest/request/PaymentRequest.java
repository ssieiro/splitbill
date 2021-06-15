package com.autentia.example.splitbill.delivery.rest.request;

import java.math.BigDecimal;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequest{
    @NotNull
    private long groupId;
    @Nullable
    private String description;
    @NotNull
    private BigDecimal amount;
    @Nullable
    private String payedTo;
}
