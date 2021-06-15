package com.autentia.example.splitbill.delivery.rest.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

import com.autentia.example.splitbill.domain.payment.Payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse{
    @NotNull
    private String userId;
    @NotNull
    private long groupId;
    @Nullable
    private LocalDateTime paymentTs;
    @Nullable
    String description;
    @NotNull
    private BigDecimal amount;
    @Nullable
    private String payedTo;

    public static PaymentResponse of(final Payment payment){
        return PaymentResponse.builder()
                .userId(payment.getId().getUserId())
                .groupId(payment.getId().getGroupId())
                .amount(payment.getAmount())
                .description(payment.getDescription())
                .paymentTs(payment.getPaymentTs())
                .build();
    }
}

