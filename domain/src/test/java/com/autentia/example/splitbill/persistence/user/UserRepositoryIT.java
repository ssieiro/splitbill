package com.autentia.example.splitbill.persistence.user;

import static org.assertj.core.api.Assertions.assertThat;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import com.autentia.example.splitbill.domain.user.User;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;

@MicronautTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserRepositoryIT{

    private static final String TEST_EXISTING_USER_ID = "carlos";
    private static final String TEST_USER_ID = "john";
    private static final String TEST_USER_NAME = "John Doe";
    private static final String Test_USER_PWD = "john123";

    private final User user = User.builder()
            .userId(TEST_USER_ID)
            .name(TEST_USER_NAME)
            .password(Test_USER_PWD)
            .build();
    @Inject
    UserRepository sut;


    @Test
    void shouldExistsAfterUserCreation() {
        assertThat(sut.existsByUserId(TEST_USER_ID)).isFalse();
        assertThat(sut.save(user)).isEqualTo(user);
        assertThat(sut.existsByUserId(TEST_USER_ID)).isTrue();
    }

}