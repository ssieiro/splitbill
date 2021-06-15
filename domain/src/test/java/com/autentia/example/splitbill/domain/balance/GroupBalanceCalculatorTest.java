package com.autentia.example.splitbill.domain.balance;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class GroupBalanceCalculatorTest{

    private GroupBalanceCalculator sut = new GroupBalanceCalculator();

    @Test
    public void shoutCalculateBalanceFromPaymentBalanceList() {
        List<UserPaymentBalance> usersPaymentBalances =  List.of(
                UserPaymentBalance.builder().userId("carlos").amountPayed(BigDecimal.valueOf(100)).amountReceived(BigDecimal.valueOf(25)).pendingAmount(BigDecimal.valueOf(-25)).build(),
                UserPaymentBalance.builder().userId("pedro").amountPayed(BigDecimal.valueOf(25)).amountReceived(BigDecimal.ZERO).pendingAmount(BigDecimal.valueOf(25)).build());

        List<PaymentBalance> result = sut.calculatePendingGroupPayments(usersPaymentBalances);
        Assertions.assertThat(result)
                .isNotEmpty()
                .size().isEqualTo(1);
        Assertions.assertThat(result).contains(PaymentBalance.builder().user("pedro").amount(BigDecimal.valueOf(25)).payTo("carlos").build());

    }

    @Test
    public void shoutReturnEmptyListWhenThereIsNotPaymentsInGroups() {
        List<UserPaymentBalance> usersPaymentBalances =  List.of(
                UserPaymentBalance.builder().userId("carlos").amountPayed(BigDecimal.ZERO).amountReceived(BigDecimal.ZERO).pendingAmount(BigDecimal.ZERO).build(),
                UserPaymentBalance.builder().userId("pedro").amountPayed(BigDecimal.ZERO).amountReceived(BigDecimal.ZERO).pendingAmount(BigDecimal.ZERO).build());

        List<PaymentBalance> result = sut.calculatePendingGroupPayments(usersPaymentBalances);
        Assertions.assertThat(result).isEmpty();

    }

    @Test
    public void shoutReturnBalanceOKWhenThereIsOnlyPaymentsToGroups() {
        List<UserPaymentBalance> usersPaymentBalances =  List.of(
                UserPaymentBalance.builder().userId("carlos").amountPayed(BigDecimal.ZERO).amountReceived(BigDecimal.valueOf(10)).pendingAmount(BigDecimal.valueOf(5)).build(),
                UserPaymentBalance.builder().userId("pedro").amountPayed(BigDecimal.TEN).amountReceived(BigDecimal.ZERO).pendingAmount(BigDecimal.valueOf(-5)).build());

        List<PaymentBalance> result = sut.calculatePendingGroupPayments(usersPaymentBalances);
        Assertions.assertThat(result)
                .isNotEmpty()
                .hasSize(1)
                .contains(  PaymentBalance
                            .builder()
                            .user("carlos")
                            .amount(BigDecimal.valueOf(5))
                            .payTo("pedro")
                            .build()
                );

    }
}