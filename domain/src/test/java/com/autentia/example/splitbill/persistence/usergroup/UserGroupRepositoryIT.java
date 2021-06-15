package com.autentia.example.splitbill.persistence.usergroup;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import com.autentia.example.splitbill.domain.usergroup.UserGroup;
import com.autentia.example.splitbill.domain.usergroup.UserGroupID;

import io.micronaut.data.exceptions.DataAccessException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;

@MicronautTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserGroupRepositoryIT{
    private static final long TEST_EXISTING_GROUP_ID = 1;
    private static final String TEST_NON_ASSIGNED_USER_ID = "alfonso";
    private static final String TEST_PREVIOUSLY_ASSIGNED_USER_ID = "carlos";

    @Inject
    private UserGroupRepository sut;

    @Test
    void shouldReturnTrueIfAddUserToGroupsGoesRight() {
        UserGroup userGroup = UserGroup
                .builder()
                .userGroupID(new UserGroupID(TEST_EXISTING_GROUP_ID, TEST_NON_ASSIGNED_USER_ID))
                .build();
        assertThat(sut.save(userGroup)).isEqualTo(userGroup);
    }

    @Test
    void shouldThrowDataAccessExceptionIfAddUserToGroupsUserAlreadyExistsInGroup() {
        UserGroup userGroup = UserGroup
                .builder()
                .userGroupID(new UserGroupID(TEST_EXISTING_GROUP_ID, TEST_PREVIOUSLY_ASSIGNED_USER_ID))
                .build();

        Throwable exception = catchThrowable(() -> {
            sut.save(userGroup);
        });

        assertThat(exception)
                .isNotNull()
                .isInstanceOf(DataAccessException.class);
    }

}
