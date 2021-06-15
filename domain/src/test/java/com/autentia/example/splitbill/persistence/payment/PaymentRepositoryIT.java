package com.autentia.example.splitbill.persistence.payment;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import com.autentia.example.splitbill.domain.payment.Payment;
import com.autentia.example.splitbill.domain.usergroup.UserGroupID;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;

@MicronautTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PaymentRepositoryIT{

    private static final String TEST_USER_ID = "carlos";
    private static final long TEST_GROUP_ID = 1;
    private static final long TEST_GROUP_ID_2 = 2;
    private static final BigDecimal TEST_PAY = BigDecimal.TEN;

    @Inject
    private PaymentRepository sut;

    @Test
    void findByGroupIdShouldListAllPaymentsAssociated() {
        assertThat(sut.findAllByIdGroupId(TEST_GROUP_ID))
                .isNotEmpty()
                .hasSize(2)
                .allSatisfy(payment -> assertThat(payment.getId().getGroupId()).isEqualTo(TEST_GROUP_ID));
    }

    @Test
    void findAllUserPaymentsByIdGroupIdShouldListPaymentsAndUsersWithoutPayment() {
        assertThat(sut.findAllUserPaymentsByIdGroupId(TEST_GROUP_ID))
                .isNotEmpty()
                .hasSize(3)
                .allSatisfy(payment -> assertThat(payment.getId().getGroupId()).isEqualTo(TEST_GROUP_ID))
                .anyMatch  (payment -> payment.getAmount().compareTo(BigDecimal.ZERO) == 0);
    }


    @Test
    @Transactional
    void saveShouldSavePayments() {
        Payment payment = Payment.builder()
                .id(new UserGroupID(TEST_GROUP_ID_2, TEST_USER_ID))
                .amount(TEST_PAY)
                .paymentTs(LocalDateTime.now())
                .build();

        Payment result = sut.save(payment);
        assertThat(result).isNotNull();
        assertThat(result.getPaymentTs()).isNotNull();

    }

}