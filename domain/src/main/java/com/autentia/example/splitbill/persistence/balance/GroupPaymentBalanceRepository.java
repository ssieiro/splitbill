package com.autentia.example.splitbill.persistence.balance;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.ResultSet;

import com.autentia.example.splitbill.domain.balance.GroupPaymentBalance;

import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.jdbc.runtime.JdbcOperations;
import io.micronaut.data.model.query.builder.sql.Dialect;

@JdbcRepository(dialect = Dialect.POSTGRES)
public class GroupPaymentBalanceRepository{
    private final JdbcOperations jdbc;

    private static final String SQL_GROUP_PAYMENT_BALANCE = ""
            + " SELECT  COUNT(DISTINCT UG.USER_ID) num_users, "
            + "         COALESCE(SUM(CASE WHEN PAYED_TO IS NULL THEN amount ELSE 0 END),0)  total_amount "
            + "    FROM "
            + "        users_groups ug "
            + "         LEFT JOIN group_payments gp ON ug.user_id = gp.user_id  and ug.group_id = gp.group_id "
            + "    WHERE "
            + "        ug.group_id = ?";

    public GroupPaymentBalanceRepository(JdbcOperations jdbc){
        this.jdbc = jdbc;
    }


    public GroupPaymentBalance aggregateGroupPaymentBalance(long groupId) {

        return jdbc.prepareStatement(
                SQL_GROUP_PAYMENT_BALANCE,
                ps -> {
                    ps.setLong(1, groupId);
                    ResultSet rs = ps.executeQuery();
                    if(rs.next()){
                        BigDecimal totalBill = rs.getBigDecimal("total_amount");
                        BigDecimal numUsers = rs.getBigDecimal("num_users");

                        return GroupPaymentBalance.builder()
                                .totalBill(totalBill)
                                .numUsers(numUsers.intValue())
                                .billPerUser(totalBill.divide(numUsers, 2, RoundingMode.CEILING))
                                .build();
                    }
                    return null;
                });
    }
}
