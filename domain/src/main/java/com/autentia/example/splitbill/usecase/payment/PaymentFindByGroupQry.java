package com.autentia.example.splitbill.usecase.payment;

import java.util.List;

import com.autentia.example.splitbill.domain.payment.PaymentValidator;
import com.autentia.example.splitbill.domain.payment.Payment;
import com.autentia.example.splitbill.persistence.payment.PaymentRepository;

import io.archimedes.locator.ServiceLocator;
import io.archimedes.security.Security;
import io.archimedes.usecase.Query;

public class PaymentFindByGroupQry extends Query<List<Payment>> {

    private PaymentRepository paymentRepository;
    private PaymentValidator paymentValidator;
    private long groupId;

    public PaymentFindByGroupQry(long groupId, PaymentRepository paymentRepository, PaymentValidator paymentValidator){
        this.groupId = groupId;
        this.paymentRepository = paymentRepository;
        this.paymentValidator = paymentValidator;
    }

    public PaymentFindByGroupQry(long groupId){
        this(   groupId,
                ServiceLocator.INSTANCE.locate(PaymentRepository.class),
                ServiceLocator.INSTANCE.locate(PaymentValidator.class)
                );
    }

    @Override
    public List<Payment> run(){
        paymentValidator.validatePaymentQry(Security.INSTANCE.getUsername(), groupId);
        return paymentRepository.findAllByIdGroupId(this.groupId);
    }
}
