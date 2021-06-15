package com.autentia.example.splitbill.usecase.payment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.autentia.example.splitbill.domain.payment.PaymentValidator;
import com.autentia.example.splitbill.domain.payment.Payment;
import com.autentia.example.splitbill.persistence.payment.PaymentRepository;
import com.autentia.example.splitbill.domain.usergroup.UserGroupID;

import io.archimedes.security.Security;
import io.archimedes.security.test.FakeSecurityContext;

class PaymentFindByGroupQryTest{

    private PaymentRepository paymentRepository = Mockito.mock(PaymentRepository.class);
    private PaymentValidator paymentQryValidator = Mockito.mock(PaymentValidator.class);
    private static final String TEST_USER_ID = "carlos";
    private static final String TEST_NOTALLOWED_USER_ID = "pedro";
    private static final long TEST_GROUP_ID = 1;
    private static final BigDecimal TEST_PAY_ZERO = BigDecimal.ZERO;

    @Test
    void shouldReturnPaymentListsFromRepository () {
        Payment payment =
                Payment.builder()
                        .id(new UserGroupID(TEST_GROUP_ID, TEST_USER_ID))
                        .amount(TEST_PAY_ZERO)
                        .paymentTs(LocalDateTime.now())
                        .build();
        when(paymentRepository.findAllByIdGroupId(TEST_GROUP_ID)).thenReturn(List.of(payment));

        PaymentFindByGroupQry sut = new PaymentFindByGroupQry(TEST_GROUP_ID, paymentRepository, paymentQryValidator);

        List<Payment> payments = callPaymentFindByGroupQryAs(TEST_USER_ID, sut);
        assertThat(payments)
                .isNotEmpty()
                .hasSize(1)
                .allSatisfy(p -> assertThat(p.getId().getGroupId()).isEqualTo(TEST_GROUP_ID));
    }

    @Test
    void shouldReturnErrorIfUserNotBelongsToGroup () {

        doThrow(new SecurityException(String.format("User not Allowed: %s", TEST_NOTALLOWED_USER_ID)))
                .when(paymentQryValidator).validatePaymentQry(TEST_NOTALLOWED_USER_ID, TEST_GROUP_ID);

        PaymentFindByGroupQry sut = new PaymentFindByGroupQry(TEST_GROUP_ID, paymentRepository, paymentQryValidator);

        Throwable exception = catchThrowable(() -> {
            callPaymentFindByGroupQryAs(TEST_NOTALLOWED_USER_ID, sut);
        });

        assertThat(exception)
                .isNotNull()
                .isInstanceOf(SecurityException.class)
        .hasMessageContaining(TEST_NOTALLOWED_USER_ID);
    }

    private List<Payment> callPaymentFindByGroupQryAs(String user, PaymentFindByGroupQry sut) {
        return Security.INSTANCE.runAs(new FakeSecurityContext(user, Collections.emptySet()), sut::run);
    }

}