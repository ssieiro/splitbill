package com.autentia.example.splitbill.domain.balance;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Singleton;

import com.autentia.example.splitbill.domain.payment.Payment;

import io.micronaut.core.util.StringUtils;

@Singleton
public class UserPaymentBalanceCalculator {

    public List<UserPaymentBalance> calculateUsersPaymentBalance (List<Payment> payments, BigDecimal billPerUser) {

        Map<String, UserPaymentBalance> usersBalanceMap = new HashMap<>();

        for(Payment payment: payments){

            UserPaymentBalance userBalance = getUserBalance(payment.getId().getUserId(), usersBalanceMap);

            userBalance.addAmountPayed(payment.getAmount());
            userBalance.calculatePendingAmount(billPerUser);

            if(StringUtils.isNotEmpty(payment.getPayedTo())){
                UserPaymentBalance userPayedBalance = getUserBalance(payment.getPayedTo(), usersBalanceMap);
                userPayedBalance.addAmountReceived(payment.getAmount());
                userPayedBalance.calculatePendingAmount(billPerUser);
            }

        }

        return usersBalanceMap.values().stream().collect(Collectors.toList());

    }

    private UserPaymentBalance getUserBalance(String userId, Map<String, UserPaymentBalance> usersBalanceMap) {
        usersBalanceMap.computeIfAbsent(userId,
                s -> UserPaymentBalance.builder()
                        .userId(userId)
                        .amountPayed(BigDecimal.ZERO)
                        .amountReceived(BigDecimal.ZERO)
                        .pendingAmount(BigDecimal.ZERO)
                        .build());

        return usersBalanceMap.get(userId);
    }

}
