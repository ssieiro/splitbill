package com.autentia.example.splitbill.usecase.balance;

import java.util.List;

import com.autentia.example.splitbill.domain.balance.GroupBalanceCalculator;
import com.autentia.example.splitbill.domain.balance.UserPaymentBalanceCalculator;
import com.autentia.example.splitbill.domain.balance.GroupPaymentBalance;
import com.autentia.example.splitbill.domain.balance.UserPaymentBalance;
import com.autentia.example.splitbill.persistence.balance.GroupPaymentBalanceRepository;
import com.autentia.example.splitbill.domain.payment.Payment;
import com.autentia.example.splitbill.persistence.payment.PaymentRepository;

import io.archimedes.locator.ServiceLocator;
import io.archimedes.usecase.Query;

public class GroupPaymentsBalanceQry extends Query<GroupPaymentBalance> {

    private long groupId;
    private GroupPaymentBalanceRepository groupPaymentBalanceRepository;
    private PaymentRepository paymentRepository;
    private GroupBalanceCalculator groupBalanceCalculator;
    private UserPaymentBalanceCalculator userPaymentBalanceCalculator;

    public GroupPaymentsBalanceQry(long groupId,
            GroupPaymentBalanceRepository groupPaymentBalanceRepository,
            PaymentRepository paymentRepository, GroupBalanceCalculator groupBalanceCalculator, UserPaymentBalanceCalculator userPaymentBalanceCalculator){
        this.groupId = groupId;
        this.groupPaymentBalanceRepository = groupPaymentBalanceRepository;
        this.paymentRepository = paymentRepository;
        this.groupBalanceCalculator = groupBalanceCalculator;
        this.userPaymentBalanceCalculator = userPaymentBalanceCalculator;
    }

    public GroupPaymentsBalanceQry(long groupId) {
        this(groupId,
                ServiceLocator.INSTANCE.locate(GroupPaymentBalanceRepository.class),
                ServiceLocator.INSTANCE.locate(PaymentRepository.class),
                ServiceLocator.INSTANCE.locate(GroupBalanceCalculator.class),
                ServiceLocator.INSTANCE.locate(UserPaymentBalanceCalculator.class));
    }

    @Override
    protected GroupPaymentBalance run(){
        GroupPaymentBalance balance = groupPaymentBalanceRepository.aggregateGroupPaymentBalance(groupId);

        List<Payment> payments = paymentRepository.findAllUserPaymentsByIdGroupId(groupId);


        List<UserPaymentBalance> usersPaymentBalance = userPaymentBalanceCalculator.calculateUsersPaymentBalance(payments, balance.getBillPerUser());
        balance.setUsersPaymentBalance(usersPaymentBalance);

        balance.setPendingPayments(groupBalanceCalculator.calculatePendingGroupPayments(usersPaymentBalance));

        return balance;
    }



}
