package com.autentia.example.splitbill.persistence.usergroup;

import com.autentia.example.splitbill.domain.usergroup.UserGroup;
import com.autentia.example.splitbill.domain.usergroup.UserGroupID;

import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;

@JdbcRepository(dialect = Dialect.POSTGRES)
public interface UserGroupRepository extends CrudRepository<UserGroup, UserGroupID>{

    boolean existsByUserGroupID(UserGroupID userGroupID);

}
