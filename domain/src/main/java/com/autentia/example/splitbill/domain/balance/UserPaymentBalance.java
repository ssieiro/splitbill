package com.autentia.example.splitbill.domain.balance;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPaymentBalance{
    private String userId;
    private BigDecimal amountPayed;
    private BigDecimal amountReceived;
    private BigDecimal pendingAmount;

    public void addAmountPayed(BigDecimal amount) {
        amountPayed = amount.add(amountPayed);
    }

    public void addAmountReceived(BigDecimal amount) {
        amountReceived = amount.add(amountReceived);
    }

    public BigDecimal getTotalAmountPayed(){
        return amountPayed.subtract(amountReceived);
    }

    public void substractPendingAmount(BigDecimal amount) {
        pendingAmount = pendingAmount.subtract(amount);
    }
    public void addPendingAmount(BigDecimal amount) {
        pendingAmount = pendingAmount.add(amount);
    }

    public void calculatePendingAmount(BigDecimal billPerUser) {
        pendingAmount = billPerUser.subtract(getTotalAmountPayed());
    }

    public UserPaymentBalance(UserPaymentBalance userPaymentBalance) {
        userId = userPaymentBalance.getUserId();
        amountPayed = userPaymentBalance.getAmountPayed();
        amountReceived = userPaymentBalance.getAmountReceived();
        pendingAmount = userPaymentBalance.getPendingAmount();
    }

}
