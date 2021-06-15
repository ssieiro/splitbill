package com.autentia.example.splitbill.delivery.rest.response;

import java.math.BigDecimal;

import com.autentia.example.splitbill.domain.balance.PaymentBalance;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentBalanceResponse{
    String user;
    String payTo;
    BigDecimal amount;

    public static PaymentBalanceResponse of(final PaymentBalance paymentBalance){
        return PaymentBalanceResponse.builder()
                .user(paymentBalance.getUser())
                .payTo(paymentBalance.getPayTo())
                .amount(paymentBalance.getAmount())
                .build();
    }

}
