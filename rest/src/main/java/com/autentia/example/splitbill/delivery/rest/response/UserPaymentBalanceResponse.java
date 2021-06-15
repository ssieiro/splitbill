package com.autentia.example.splitbill.delivery.rest.response;

import java.math.BigDecimal;

import com.autentia.example.splitbill.domain.balance.UserPaymentBalance;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPaymentBalanceResponse{
    private String userId;
    private BigDecimal amountPayed;
    private BigDecimal amountReceived;
    private BigDecimal pendingAmount;

    public static UserPaymentBalanceResponse of(UserPaymentBalance userBalance) {
        return  UserPaymentBalanceResponse.builder()
                .userId(userBalance.getUserId())
                .amountPayed(userBalance.getAmountPayed())
                .amountReceived(userBalance.getAmountReceived())
                .pendingAmount(userBalance.getPendingAmount())
                .build();
    }
}
