package com.autentia.example.splitbill.persistence.payment;

import java.sql.ResultSet;
import java.util.List;
import java.util.stream.Collectors;

import com.autentia.example.splitbill.domain.payment.Payment;
import com.autentia.example.splitbill.domain.usergroup.UserGroupID;

import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.jdbc.runtime.JdbcOperations;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;

@JdbcRepository(dialect = Dialect.POSTGRES)
public abstract class PaymentRepository implements CrudRepository<Payment, UserGroupID> {

    public abstract List<Payment> findAllByIdGroupId(long groupId);

    private static final String SQL_PAYMENTS_ALL_USERS = " "
            + "  SELECT group_id, user_id, payment_ts, description, amount, payed_to "
            + "   FROM group_payments gp "
            + "   WHERE group_id = ? "
            + "   UNION ALL "
            + "   SELECT group_id, user_id, current_timestamp, 'User without payments', 0, null "
            + "   FROM "
            + "      users_groups ug "
            + "    WHERE "
            + "       group_id = ? "
            + "       AND NOT EXISTS (SELECT 1 "
            + "                         FROM group_payments gpi "
            + "                         WHERE gpi.group_id = ug.group_id AND gpi.user_id = ug.user_id )";

    private JdbcOperations jdbc;

    protected PaymentRepository(JdbcOperations jdbc){
        this.jdbc = jdbc;
    }

    public List<Payment> findAllUserPaymentsByIdGroupId(long groupId) {
        return jdbc.prepareStatement(SQL_PAYMENTS_ALL_USERS,
                ps -> {
                    ps.setLong(1, groupId);
                    ps.setLong(2, groupId);
                    ResultSet rs = ps.executeQuery();
                    return jdbc.entityStream(rs, Payment.class).collect(Collectors.toList());
                });
    }
}
