package com.autentia.example.splitbill.domain.balance;

import java.math.BigDecimal;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GroupPaymentBalance{
    private BigDecimal totalBill;
    private int numUsers;
    private BigDecimal billPerUser;
    private List<UserPaymentBalance> usersPaymentBalance;
    private List<PaymentBalance> pendingPayments;
}
