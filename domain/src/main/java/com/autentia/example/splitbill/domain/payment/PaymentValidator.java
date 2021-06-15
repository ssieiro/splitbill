package com.autentia.example.splitbill.domain.payment;

import java.math.BigDecimal;

import javax.inject.Singleton;

import com.autentia.example.splitbill.domain.usergroup.UserGroupID;
import com.autentia.example.splitbill.persistence.usergroup.UserGroupRepository;

import io.archimedes.locator.ServiceLocator;
import io.archimedes.security.Security;
import io.micronaut.core.util.StringUtils;

@Singleton
public class PaymentValidator {

    private UserGroupRepository userGroupRepository;

    public PaymentValidator(UserGroupRepository userGroupRepository){
        this.userGroupRepository = userGroupRepository;
    }

    public PaymentValidator(){
        this.userGroupRepository = ServiceLocator.INSTANCE.locate(UserGroupRepository.class);
    }

    protected boolean isValidUser(String userId){
        return userId.equals(Security.INSTANCE.getUsername());
    }

    protected boolean isValidUserGroup(String userId, long groupId){
        return userGroupRepository.existsByUserGroupID(new UserGroupID(groupId, userId));
    }

    public boolean validatePaymentQry(String userId, long groupId) throws SecurityException {
        if(!isValidUser(userId) || !isValidUserGroup(userId, groupId)) {
            throw new SecurityException(String.format("Operation not Allowed for user %s", userId));
        }
        return true;
    }

    private boolean isValidAmount(BigDecimal amount) {
        return amount != null && BigDecimal.ZERO.compareTo(amount) < 0;
    }

    public PaymentValidatorState validatePayment(Payment payment){
        if(!isValidUser(payment.getId().getUserId())) {
            return PaymentValidatorState.INVALID_USER;
        }
        if(!isValidUserGroup(payment.getId().getUserId(), payment.getId().getGroupId())) {
            return PaymentValidatorState.INVALID_GROUP;
        }
        if(StringUtils.isNotEmpty(payment.getPayedTo())
                && !isValidUserGroup(payment.getPayedTo(), payment.getId().getGroupId())) {
                return PaymentValidatorState.INVALID_PAYMENT;
        }
        if(!isValidAmount(payment.getAmount())) {
            return PaymentValidatorState.INVALID_PAYMENT;
        }
        return PaymentValidatorState.OK;
    }
}