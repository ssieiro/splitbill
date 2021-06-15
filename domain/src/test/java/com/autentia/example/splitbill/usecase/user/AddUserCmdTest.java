package com.autentia.example.splitbill.usecase.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;

import org.junit.jupiter.api.Test;

import com.autentia.example.splitbill.domain.user.User;
import com.autentia.example.splitbill.persistence.user.UserRepository;

import io.archimedes.security.Security;
import io.archimedes.security.test.FakeSecurityContext;

public class AddUserCmdTest {

    private static final String TEST_USER_ID = "john";
    private static final String TEST_USER_NAME = "john doe";
    private static final String TEST_USER_PASSWORD = "john123";

    private static final String TEST_EXISTING_USER_ID = "carlos";

    private final UserRepository userRepository = mock(UserRepository.class);

    private final User user = User
            .builder()
            .userId(TEST_USER_ID)
            .name(TEST_USER_NAME)
            .password(TEST_USER_PASSWORD)
            .build();

    @Test
    void shouldCreateUserWhenEverythingIsRight() {

        when(userRepository.save(user)).thenReturn(user);
        AddUserCmd sut = new AddUserCmd(user, userRepository);
        AddUserCmdStatus status = callAddUserCmdAs (TEST_USER_ID, sut);

        assertThat(status).isEqualTo(AddUserCmdStatus.OK);

    }

    @Test
    void shouldReturnUSERALREADYEXISTSWhenUserIdIsDuplicated () {
        User user = User
            .builder()
            .userId(TEST_EXISTING_USER_ID)
            .name(TEST_USER_NAME)
            .password(TEST_USER_PASSWORD)
            .build();

        when(userRepository.existsByUserId(TEST_EXISTING_USER_ID)).thenReturn(true);

        AddUserCmd sut = new AddUserCmd(user, userRepository);
        AddUserCmdStatus status = callAddUserCmdAs (TEST_USER_ID, sut);

        assertThat(status).isEqualTo(AddUserCmdStatus.USER_ALREADY_EXISTS);
    }

    private AddUserCmdStatus callAddUserCmdAs(String user, AddUserCmd sut){
        return Security.INSTANCE.runAs(new FakeSecurityContext(user, Collections.emptySet()), sut::run);
    }
}
