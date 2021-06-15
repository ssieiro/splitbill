package com.autentia.example.splitbill.delivery.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import com.autentia.example.splitbill.delivery.rest.request.PaymentRequest;
import com.autentia.example.splitbill.usecase.payment.PaymentCmd;
import com.autentia.example.splitbill.domain.payment.PaymentValidatorState;

import io.archimedes.locator.ServiceLocatorTest;
import io.archimedes.security.Security;
import io.archimedes.security.test.FakeSecurityContext;
import io.archimedes.usecase.UseCaseBus;
import io.micronaut.http.HttpStatus;

@ServiceLocatorTest
class GroupPaymentControllerTest{

    private UseCaseBus bus = mock(UseCaseBus.class);
    private GroupPaymentController sut = new GroupPaymentController(bus);
    private static final long TEST_GROUP_ID = 1;
    private static final PaymentRequest TEST_PAYMENT =
            PaymentRequest
                    .builder()
                    .groupId(TEST_GROUP_ID)
                    .amount(new BigDecimal(10))
                    .build();

    @Test
    void shouldReturn_HttpACCEPTED_WhenPaymentIsDone (){
        when(bus.invoke(any(PaymentCmd.class))).thenReturn(PaymentValidatorState.OK);

        HttpStatus httpStatus = callPayAs("carlos", TEST_GROUP_ID, TEST_PAYMENT);

        assertThat(httpStatus.getCode()).isEqualTo(HttpStatus.ACCEPTED.getCode());
    }

    @Test
    void shouldReturn_HttpINTERNALSERVERERROR_WhenPaymentIsNotDone() {
        when(bus.invoke(any(PaymentCmd.class))).thenReturn(PaymentValidatorState.INTERNAL_ERROR);

        HttpStatus httpStatus = callPayAs("carlos", TEST_GROUP_ID, TEST_PAYMENT);

        assertThat(httpStatus.getCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.getCode());
    }

    @Test
    void shouldReturn_HttpUNAUTHORIZED_WhenUserIsInvalid() {
        when(bus.invoke(any(PaymentCmd.class))).thenReturn(PaymentValidatorState.INVALID_USER);

        HttpStatus httpStatus = callPayAs("carlos", TEST_GROUP_ID, TEST_PAYMENT);

        assertThat(httpStatus.getCode()).isEqualTo(HttpStatus.UNAUTHORIZED.getCode());
    }

    @Test
    void shouldReturn_HttpUNAUTHORIZED_WhenGroupIsInvalid() {
        when(bus.invoke(any(PaymentCmd.class))).thenReturn(PaymentValidatorState.INVALID_GROUP);

        HttpStatus httpStatus = callPayAs("carlos", TEST_GROUP_ID, TEST_PAYMENT);

        assertThat(httpStatus.getCode()).isEqualTo(HttpStatus.UNAUTHORIZED.getCode());
    }

    @Test
    void shouldReturn_HttpBADREQUEST_WhenPaymentInvalid() {
        when(bus.invoke(any(PaymentCmd.class))).thenReturn(PaymentValidatorState.INVALID_PAYMENT);

        HttpStatus httpStatus = callPayAs("carlos", TEST_GROUP_ID, TEST_PAYMENT);

        assertThat(httpStatus.getCode()).isEqualTo(HttpStatus.BAD_REQUEST.getCode());
    }

    private HttpStatus callPayAs(String user, long groupId, PaymentRequest paymentRequest) {
        return Security.INSTANCE.runAs(new FakeSecurityContext(user, Collections.emptySet()), () -> sut.pay(groupId, paymentRequest));
    }
}