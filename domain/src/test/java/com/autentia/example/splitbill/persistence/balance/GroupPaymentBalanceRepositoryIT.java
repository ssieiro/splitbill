package com.autentia.example.splitbill.persistence.balance;

import static org.assertj.core.api.Assertions.assertThat;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import com.autentia.example.splitbill.domain.balance.GroupPaymentBalance;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;

@MicronautTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GroupPaymentBalanceRepositoryIT{

    @Inject
    GroupPaymentBalanceRepository sut;

    private static final long TEST_GROUP_ID = 1;

    @Test
    public void shouldReturnTotalBillAndBillPerUser(){

        GroupPaymentBalance balance = sut.aggregateGroupPaymentBalance(TEST_GROUP_ID);
        assertThat(balance).isNotNull();

        assertThat(balance.getTotalBill()).isEqualByComparingTo("15");
        assertThat(balance.getBillPerUser()).isEqualByComparingTo("5");

    }
}