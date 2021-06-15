package com.autentia.example.splitbill.persistence.group;

import com.autentia.example.splitbill.domain.group.Group;

import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;

@JdbcRepository(dialect = Dialect.POSTGRES)
public interface GroupRepository extends CrudRepository<Group, Long> {
   int countByGroupId(long groupId);
}
