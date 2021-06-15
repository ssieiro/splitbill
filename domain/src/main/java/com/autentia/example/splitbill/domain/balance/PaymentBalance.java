package com.autentia.example.splitbill.domain.balance;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentBalance {
    private final String user;
    private final String payTo;
    private final BigDecimal amount;

}
