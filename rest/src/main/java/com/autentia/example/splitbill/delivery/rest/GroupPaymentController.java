package com.autentia.example.splitbill.delivery.rest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.autentia.example.splitbill.delivery.rest.request.PaymentRequest;
import com.autentia.example.splitbill.delivery.rest.response.PaymentResponse;
import com.autentia.example.splitbill.domain.payment.PaymentValidatorState;
import com.autentia.example.splitbill.domain.payment.Payment;
import com.autentia.example.splitbill.usecase.payment.PaymentCmd;
import com.autentia.example.splitbill.usecase.payment.PaymentFindByGroupQry;
import com.autentia.example.splitbill.domain.usergroup.UserGroupID;

import io.archimedes.security.Security;
import io.archimedes.usecase.UseCaseBus;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;

@Controller("/splitbill/groups")
@Secured(SecurityRule.IS_AUTHENTICATED)
public class GroupPaymentController{

    private static final Logger log = LoggerFactory.getLogger(GroupPaymentController.class);

    private final UseCaseBus bus;

    public GroupPaymentController(
            UseCaseBus bus){
        this.bus = bus;
    }

    @Post("/{groupId}/payments")
    public HttpStatus pay(@PathVariable long groupId, @Body PaymentRequest paymentRequest) {
        Payment payment = Payment.builder()
                .id(new UserGroupID(paymentRequest.getGroupId(), Security.INSTANCE.getUsername()))
                .description(paymentRequest.getDescription())
                .amount(paymentRequest.getAmount())
                .payedTo(paymentRequest.getPayedTo())
                .paymentTs(LocalDateTime.now())
                .build();

        if(groupId != paymentRequest.getGroupId()) {
            return HttpStatus.BAD_REQUEST;
        }

        PaymentValidatorState paymentCmdStatus = bus.invoke(new PaymentCmd(payment));

        log.info("Payment: {} ## Status: {}",payment , paymentCmdStatus);
         switch(paymentCmdStatus){
            case OK:
                return HttpStatus.ACCEPTED;
            case INVALID_PAYMENT:
                return HttpStatus.BAD_REQUEST;
            case INVALID_GROUP:
            case INVALID_USER:
                return HttpStatus.UNAUTHORIZED;
             case INTERNAL_ERROR:
             default:
                 return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }

    @Get("/{groupId}/payments")
    public HttpResponse<List<PaymentResponse>> getGroupPayments(final long groupId) {

         List<Payment> paymentList = bus.invoke(new PaymentFindByGroupQry(groupId));

         return HttpResponse.ok(paymentList.stream().map(PaymentResponse::of).collect(Collectors.toList()));

    }


}
