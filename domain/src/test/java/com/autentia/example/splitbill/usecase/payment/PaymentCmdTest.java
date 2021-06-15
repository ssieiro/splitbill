package com.autentia.example.splitbill.usecase.payment;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.autentia.example.splitbill.domain.payment.PaymentValidatorState;
import com.autentia.example.splitbill.domain.payment.Payment;
import com.autentia.example.splitbill.persistence.payment.PaymentRepository;
import com.autentia.example.splitbill.domain.payment.PaymentValidator;
import com.autentia.example.splitbill.domain.usergroup.UserGroupID;

import io.archimedes.security.Security;
import io.archimedes.security.test.FakeSecurityContext;

class PaymentCmdTest {

    private PaymentRepository paymentRepository = Mockito.mock(PaymentRepository.class);
    private PaymentValidator paymentValidator = Mockito.mock(PaymentValidator.class);

    private static final String TEST_USER_ID = "carlos";
    private static final long TEST_GROUP_ID = 1;
    private static final BigDecimal TEST_PAY = BigDecimal.TEN;
    private static final BigDecimal TEST_PAY_ZERO = BigDecimal.ZERO;

    @Test
    void shouldValidatePaymentBeforeStorePay() {
        Payment payment =
                Payment.builder()
                       .id(new UserGroupID(TEST_GROUP_ID, TEST_USER_ID))
                       .amount(TEST_PAY_ZERO)
                       .paymentTs(LocalDateTime.now())
                       .build();
        when(paymentRepository.save(payment)).thenReturn(payment);
        when(paymentValidator.validatePayment(payment)).thenReturn(PaymentValidatorState.INVALID_PAYMENT);

        PaymentCmd sut = new PaymentCmd(payment, paymentRepository, paymentValidator);
        PaymentValidatorState result = callPaymentCmdAs(TEST_USER_ID, sut);

        verify(paymentRepository, times(0)).save(payment);
        assertThat(result).isEqualTo(PaymentValidatorState.INVALID_PAYMENT);
    }

    @Test
    void shouldStoreAfterValidatingPay(){
        Payment payment =
                Payment.builder()
                        .id(new UserGroupID(TEST_GROUP_ID, TEST_USER_ID))
                        .amount(TEST_PAY)
                        .paymentTs(LocalDateTime.now())
                        .build();
        when(paymentRepository.save(payment)).thenReturn(payment);
        when(paymentValidator.validatePayment(payment)).thenReturn(PaymentValidatorState.OK);

        PaymentCmd sut = new PaymentCmd(payment, paymentRepository, paymentValidator);
        PaymentValidatorState result = callPaymentCmdAs(TEST_USER_ID, sut);

        assertThat(result).isEqualTo(PaymentValidatorState.OK);
    }

    private PaymentValidatorState callPaymentCmdAs(String user, PaymentCmd sut) {
        return Security.INSTANCE.runAs(new FakeSecurityContext(user, Collections.emptySet()), sut::run);
    }


}