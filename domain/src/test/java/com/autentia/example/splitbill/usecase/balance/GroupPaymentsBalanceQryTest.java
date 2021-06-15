package com.autentia.example.splitbill.usecase.balance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.autentia.example.splitbill.domain.balance.GroupBalanceCalculator;
import com.autentia.example.splitbill.domain.balance.UserPaymentBalanceCalculator;
import com.autentia.example.splitbill.domain.balance.GroupPaymentBalance;
import com.autentia.example.splitbill.domain.balance.PaymentBalance;
import com.autentia.example.splitbill.domain.balance.UserPaymentBalance;
import com.autentia.example.splitbill.persistence.balance.GroupPaymentBalanceRepository;
import com.autentia.example.splitbill.domain.payment.Payment;
import com.autentia.example.splitbill.persistence.payment.PaymentRepository;
import com.autentia.example.splitbill.domain.usergroup.UserGroupID;

import io.archimedes.security.Security;
import io.archimedes.security.test.FakeSecurityContext;

class GroupPaymentsBalanceQryTest{

    private static final long TEST_GROUP_ID = 1;
    private static final String TEST_USER_ID = "carlos";

    private GroupPaymentBalanceRepository groupPaymentBalanceRepository = mock(GroupPaymentBalanceRepository.class);
    private PaymentRepository paymentRepository = mock(PaymentRepository.class);
    // Calculators not mocked:
    private GroupBalanceCalculator groupBalanceCalculator = new GroupBalanceCalculator();
    private UserPaymentBalanceCalculator userPaymentBalanceCalculator = new UserPaymentBalanceCalculator();

    private List<Payment> mockedPayments () {
        return Arrays.asList(
                Payment.builder().id(new UserGroupID(1, "carlos")).amount(BigDecimal.TEN).description("test01").build(),
                Payment.builder().id(new UserGroupID(1, "pedro")).amount(BigDecimal.valueOf(2)).build(),
                Payment.builder().id(new UserGroupID(1, "pedro")).amount(BigDecimal.valueOf(2)).payedTo("carlos").build()
        );

    }
    @Test
    public void shouldReturnTotalBillAndBillPerUser () {
        when(groupPaymentBalanceRepository.aggregateGroupPaymentBalance(TEST_GROUP_ID))
                .thenReturn(
                        GroupPaymentBalance
                                .builder()
                                .totalBill(BigDecimal.valueOf(12))
                                .billPerUser(BigDecimal.valueOf(6))
                                .numUsers(2)
                                .build());

        when(paymentRepository.findAllUserPaymentsByIdGroupId(TEST_GROUP_ID)).thenReturn(mockedPayments());

        GroupPaymentsBalanceQry sutQry = new GroupPaymentsBalanceQry(TEST_GROUP_ID,
                groupPaymentBalanceRepository,
                paymentRepository,
                groupBalanceCalculator,
                userPaymentBalanceCalculator);

        GroupPaymentBalance balance = callGroupPaymentsBalanceQry(TEST_USER_ID, TEST_GROUP_ID, sutQry);
        assertThat(balance).isNotNull();
        assertThat(balance.getTotalBill()).isEqualByComparingTo("12");
        assertThat(balance.getBillPerUser()).isEqualByComparingTo("6");

        assertThat(balance.getUsersPaymentBalance())
                .isNotEmpty()
                .contains(UserPaymentBalance.builder().userId("carlos").amountPayed(BigDecimal.valueOf(10)).amountReceived(BigDecimal.valueOf(2)).pendingAmount(BigDecimal.valueOf(-2)).build())
                .contains(UserPaymentBalance.builder().userId("pedro").amountPayed(BigDecimal.valueOf(4)).amountReceived(BigDecimal.valueOf(0)).pendingAmount(BigDecimal.valueOf(2)).build())
                .size().isEqualTo(2);

        assertThat(balance.getPendingPayments())
                .isNotEmpty()
                .contains(PaymentBalance.builder().user("pedro").payTo("carlos").amount(BigDecimal.valueOf(2)).build())
                .size().isEqualTo(1);

    }


    private GroupPaymentBalance callGroupPaymentsBalanceQry(String user, long groupId, GroupPaymentsBalanceQry sut){
        return Security.INSTANCE.runAs(new FakeSecurityContext(user, Collections.emptySet()), sut::run);
    }
}