package com.autentia.example.splitbill.usecase.payment;

import com.autentia.example.splitbill.domain.payment.PaymentValidatorState;
import com.autentia.example.splitbill.domain.payment.Payment;
import com.autentia.example.splitbill.persistence.payment.PaymentRepository;
import com.autentia.example.splitbill.domain.payment.PaymentValidator;

import io.archimedes.locator.ServiceLocator;
import io.archimedes.usecase.Command;

public class PaymentCmd extends Command<PaymentValidatorState>{

    private final PaymentRepository paymentRepository;
    private final Payment payment;
    private final PaymentValidator paymentValidator;

    public PaymentCmd(Payment payment, PaymentRepository paymentRepository, PaymentValidator paymentValidator){
        this.paymentRepository = paymentRepository;
        this.paymentValidator = paymentValidator;
        this.payment = payment;
    }

    public PaymentCmd(Payment payment) {
        this (  payment,
                ServiceLocator.INSTANCE.locate(PaymentRepository.class),
                ServiceLocator.INSTANCE.locate(PaymentValidator.class)
        );
    }

    @Override
    protected PaymentValidatorState run(){
        PaymentValidatorState result = paymentValidator.validatePayment(payment);
        if(!result.equals(PaymentValidatorState.OK)) {
            return result;
        }

        paymentRepository.save(this.payment);
        return PaymentValidatorState.OK;

    }
}
