package com.autentia.example.splitbill.domain.payment;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.autentia.example.splitbill.domain.usergroup.UserGroupID;
import com.autentia.example.splitbill.persistence.usergroup.UserGroupRepository;

import io.archimedes.security.Security;
import io.archimedes.security.test.FakeSecurityContext;

class PaymentValidatorTest{



    private static final String TEST_USER_ID = "carlos";
    private static final String TEST_USER_ID_2 = "fernando";
    private static final String TEST_NOTALLOWED_USER_ID = "fernando";
    private static final long TEST_GROUP_ID = 1;
    private static final long TEST_NOTALLOWED_GROUP_ID = 1;
    private static final BigDecimal TEST_PAYMENT_AMOUNT = BigDecimal.TEN;
    private static final String TEST_PAYMENT_DESCRIPTION = "Example of Payment";
    private static final BigDecimal TEST_PAY_ZERO = BigDecimal.ZERO;
    private static final String TEST_PAYED_TO_WRONG_USER = "alfredo";

    private UserGroupRepository userGroupRepository = Mockito.mock(UserGroupRepository.class);

    @Test
    void shouldThrowSecurityExceptionWhenUserIsDifferentFromSecurityContext() {
        PaymentValidator sut = new PaymentValidator(userGroupRepository);

        Throwable exception = catchThrowable(() ->
                callPaymentValidatorAs(TEST_USER_ID, TEST_NOTALLOWED_USER_ID, TEST_GROUP_ID, sut)
        );

        Assertions.assertThat(exception)
                .isNotNull()
                .isInstanceOf(SecurityException.class)
                .hasMessageContaining(TEST_NOTALLOWED_USER_ID);

    }

    @Test
    void shouldThrowSecurityExceptionWhenUserIsNotInGroup() {
        when(userGroupRepository.existsByUserGroupID(new UserGroupID(TEST_GROUP_ID, TEST_USER_ID))).thenReturn(true);
        PaymentValidator sut = new PaymentValidator(userGroupRepository);

        Throwable exception = catchThrowable(() ->
                callPaymentValidatorAs(TEST_USER_ID, TEST_NOTALLOWED_USER_ID, TEST_NOTALLOWED_GROUP_ID, sut)
        );

        Assertions.assertThat(exception)
                .isNotNull()
                .isInstanceOf(SecurityException.class)
                .hasMessageContaining(TEST_NOTALLOWED_USER_ID);
    }

    private void callPaymentValidatorAs(String user, String userId, long groupId, PaymentValidator sut ) {
        Security.INSTANCE.runAs(new FakeSecurityContext(user, Collections.emptySet()), () -> sut.validatePaymentQry(userId, groupId));
    }

    @Test
    void shouldReturn_INVALID_USER_WhenPaymentUserIsDifferentFromSecurityContext() {
        Payment payment = Payment.builder()
                .id(new UserGroupID(TEST_GROUP_ID, TEST_USER_ID))
                .amount(TEST_PAYMENT_AMOUNT)
                .paymentTs(LocalDateTime.now())
                .build();
        PaymentValidator sut = new PaymentValidator(userGroupRepository);

        AssertionsForClassTypes
                .assertThat(callPaymentValidatorAs(TEST_USER_ID_2, sut, payment)).isEqualTo(PaymentValidatorState.INVALID_USER);
    }

    @Test
    void shouldReturn_INVALID_GROUP_WhenPaymentUserIsNotInPaymentGroup() {
        Payment payment = Payment.builder()
                .id(new UserGroupID(TEST_GROUP_ID, TEST_USER_ID))
                .amount(TEST_PAYMENT_AMOUNT)
                .paymentTs(LocalDateTime.now())
                .build();
        when(userGroupRepository.existsByUserGroupID(new UserGroupID(TEST_GROUP_ID, TEST_USER_ID))).thenReturn(false);
        PaymentValidator sut = new PaymentValidator(userGroupRepository);

        assertThat(callPaymentValidatorAs(TEST_USER_ID, sut, payment)).isEqualTo(PaymentValidatorState.INVALID_GROUP);
    }

    @Test
    void shouldReturn_INVALID_PAYMENT_WhenPayZero() {
        Payment payment = Payment.builder()
                .id(new UserGroupID(TEST_GROUP_ID, TEST_USER_ID))
                .amount(TEST_PAY_ZERO)
                .paymentTs(LocalDateTime.now())
                .build();
        when(userGroupRepository.existsByUserGroupID(new UserGroupID(TEST_GROUP_ID, TEST_USER_ID))).thenReturn(true);
        PaymentValidator sut = new PaymentValidator(userGroupRepository);

        assertThat(callPaymentValidatorAs(TEST_USER_ID, sut, payment)).isEqualTo(PaymentValidatorState.INVALID_PAYMENT);
    }

    @Test
    void shouldReturn_INVALID_PAYMENT_WhenPayedToUserDoesntBelongsToGroup() {
        Payment payment = Payment.builder()
                .id(new UserGroupID(TEST_GROUP_ID, TEST_USER_ID))
                .amount(TEST_PAY_ZERO)
                .payedTo(TEST_PAYED_TO_WRONG_USER)
                .paymentTs(LocalDateTime.now())
                .build();
        when(userGroupRepository.existsByUserGroupID(new UserGroupID(TEST_GROUP_ID, TEST_USER_ID))).thenReturn(true);
        when(userGroupRepository.existsByUserGroupID(new UserGroupID(TEST_GROUP_ID, TEST_PAYED_TO_WRONG_USER))).thenReturn(false);
        PaymentValidator sut = new PaymentValidator(userGroupRepository);

        assertThat(callPaymentValidatorAs(TEST_USER_ID, sut, payment)).isEqualTo(PaymentValidatorState.INVALID_PAYMENT);
    }

    @Test
    void shouldReturn_OK_WhenEverythingIsRight() {
        Payment payment = Payment.builder()
                .id(new UserGroupID(TEST_GROUP_ID, TEST_USER_ID))
                .description(TEST_PAYMENT_DESCRIPTION)
                .amount(TEST_PAYMENT_AMOUNT)
                .paymentTs(LocalDateTime.now())
                .build();
        when(userGroupRepository.existsByUserGroupID(new UserGroupID(TEST_GROUP_ID, TEST_USER_ID))).thenReturn(true);
        PaymentValidator sut = new PaymentValidator(userGroupRepository);

        assertThat(callPaymentValidatorAs(TEST_USER_ID, sut, payment)).isEqualTo(PaymentValidatorState.OK);
    }

    private PaymentValidatorState callPaymentValidatorAs(String user, PaymentValidator sut, Payment payment) {
        return Security.INSTANCE.runAs(new FakeSecurityContext(user, Collections.emptySet()), () -> sut.validatePayment(payment));
    }

}