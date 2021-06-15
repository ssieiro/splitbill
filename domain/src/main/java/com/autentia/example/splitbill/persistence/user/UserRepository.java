package com.autentia.example.splitbill.persistence.user;

import com.autentia.example.splitbill.domain.user.User;

import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;

@JdbcRepository(dialect = Dialect.POSTGRES)
public interface UserRepository extends CrudRepository<User, String> {
    boolean existsByUserId(String userId);

    boolean existsByUserIdAndPassword(String userId, String password);
}
