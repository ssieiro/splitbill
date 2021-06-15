package com.autentia.example.splitbill.domain.balance;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class GroupBalanceCalculator{

    private static final Logger log = LoggerFactory.getLogger(GroupBalanceCalculator.class);

    public List<PaymentBalance> calculatePendingGroupPayments (List<UserPaymentBalance> usersPaymentBalance){
        List<PaymentBalance> pendingPayments = new ArrayList<>();

        BigDecimal totalPending = usersPaymentBalance.parallelStream()
                .map(UserPaymentBalance::getPendingAmount)
                .filter(pendingAmount -> pendingAmount.compareTo(BigDecimal.ZERO)>0)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        log.debug("Total Pending: {}", totalPending);

        if( totalPending.compareTo(BigDecimal.ZERO) == 0) {
            log.debug("++++ Group without pending payments.");
            return Collections.emptyList();
        }

        List<UserPaymentBalance> userBalancesSorted = usersPaymentBalance.parallelStream()
                .map(UserPaymentBalance::new)
                .sorted(Comparator.comparing(UserPaymentBalance::getTotalAmountPayed))
                .collect(Collectors.toList());

        ListIterator<UserPaymentBalance> it = userBalancesSorted.listIterator(0);

        while(it.hasNext() && totalPending.compareTo(BigDecimal.ZERO) > 0) {

            UserPaymentBalance userBalance = it.next();
            log.debug("++++ Processing  {} [payed: {}; pending: {}]", userBalance.getUserId(), userBalance.getAmountPayed(), userBalance.getPendingAmount());

            if(userBalance.getPendingAmount().compareTo(BigDecimal.ZERO)<=0) {
                log.debug("++++++ {} is OK", userBalance.getUserId() );
                it.remove();
                continue;
            }

            totalPending = calculatePendingUserPayments(pendingPayments, totalPending, it, userBalance, userBalancesSorted);

            if(userBalance.getPendingAmount().compareTo(BigDecimal.ZERO)<=0){
                log.debug("++++++ {} is OK", userBalance.getUserId());
                it.remove();
            }

        }


        return pendingPayments;
    }




    private BigDecimal calculatePendingUserPayments(List<PaymentBalance> pendingPayments, BigDecimal totalPending,
            ListIterator<UserPaymentBalance> it, UserPaymentBalance userBalance, List<UserPaymentBalance> userBalancesSorted){

        ListIterator<UserPaymentBalance> it2 = userBalancesSorted.listIterator(it.nextIndex());

        while(it2.hasNext() && userBalance.getPendingAmount().compareTo(BigDecimal.ZERO) > 0) {
            UserPaymentBalance destUserBalance = it2.next();
            log.debug("++++++++ Searching for pay: {} [payed: {}; pending: {}]", destUserBalance.getUserId(), destUserBalance.getAmountPayed(), destUserBalance.getPendingAmount());

            if(destUserBalance.getPendingAmount().compareTo(BigDecimal.ZERO) < 0){
                BigDecimal amountToPay = calcAmountToPay(userBalance.getPendingAmount(),destUserBalance.getPendingAmount());

                if(amountToPay.compareTo(BigDecimal.ZERO) > 0){
                    totalPending = totalPending.subtract(amountToPay);
                    log.debug("++++++++++ {} pay to {} ({}) [TotalPending: {}]", userBalance.getUserId(), destUserBalance.getUserId(), amountToPay,
                            totalPending);
                    addPendingPayment(userBalance.getUserId(), destUserBalance.getUserId(), amountToPay,
                            pendingPayments);

                    userBalance.addAmountPayed(amountToPay);
                    userBalance.substractPendingAmount(amountToPay);

                    destUserBalance.addAmountReceived(amountToPay);
                    destUserBalance.addPendingAmount(amountToPay);

                }

            }

        }
        return totalPending;
    }

    private void addPendingPayment(String user, String payedTo, BigDecimal amountToPay, List<PaymentBalance> pendingPayments) {
        pendingPayments.add(PaymentBalance.builder()
                .user(user)
                .payTo(payedTo)
                .amount(amountToPay)
                .build());
    }


    private BigDecimal calcAmountToPay(final BigDecimal pendingOrigin, final BigDecimal negativePendingDest){
        BigDecimal pendingDest = negativePendingDest.abs();
        if(pendingOrigin.compareTo(pendingDest) < 0) {
            return pendingOrigin;
        }else{
            return pendingDest;
        }
    }

}
