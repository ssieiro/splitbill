package com.autentia.example.splitbill.delivery.rest.response;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import com.autentia.example.splitbill.domain.balance.GroupPaymentBalance;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupPaymentBalanceResponse{
    @NotNull
    private BigDecimal totalBill;
    @NotNull
    private int numUsers;
    @NotNull
    private BigDecimal billPerUser;
    @NotNull
    private List<UserPaymentBalanceResponse> userPaymentBalance;
    @NotNull
    private List<PaymentBalanceResponse> pendingPayments;

    public static GroupPaymentBalanceResponse of(final GroupPaymentBalance balance){
        return GroupPaymentBalanceResponse.builder()
                        .totalBill(balance.getTotalBill())
                        .numUsers(balance.getNumUsers())
                        .billPerUser(balance.getBillPerUser())
                        .userPaymentBalance(balance.getUsersPaymentBalance()
                                .stream().map(UserPaymentBalanceResponse::of)
                                .collect(Collectors.toList()))
                        .pendingPayments(balance.getPendingPayments()
                                .stream().map(PaymentBalanceResponse::of)
                                .collect(Collectors.toList()))
                        .build();
    }
}
