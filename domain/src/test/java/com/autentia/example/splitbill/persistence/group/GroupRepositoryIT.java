package com.autentia.example.splitbill.persistence.group;

import static org.assertj.core.api.Assertions.assertThat;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import com.autentia.example.splitbill.domain.group.Group;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;

@MicronautTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GroupRepositoryIT{

    private static final long TEST_GROUP_ID = 99;
    private static final String TEST_GROUP_NAME = "TestGroup";


    private final Group group = Group.builder()
            .groupId(TEST_GROUP_ID)
            .name(TEST_GROUP_NAME)
            .build();
    @Inject
    GroupRepository sut;


    @Test
    void shouldExistsAfterUserCreation() {
        assertThat(sut.countByGroupId(TEST_GROUP_ID)).isZero();
        assertThat(sut.save(group)).isEqualTo(group);
        assertThat(sut.countByGroupId(TEST_GROUP_ID)).isEqualTo(1);
    }
    

}