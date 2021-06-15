package com.autentia.example.splitbill.usecase.group;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;

import org.junit.jupiter.api.Test;

import com.autentia.example.splitbill.usecase.group.status.CreateGroupCmdStatus;
import com.autentia.example.splitbill.domain.group.Group;
import com.autentia.example.splitbill.persistence.group.GroupRepository;

import io.archimedes.security.Security;
import io.archimedes.security.test.FakeSecurityContext;

public class CreateGroupCmdTest{

    private static final String TEST_USER_ID = "carlos";
    private static final long TEST_GROUP_ID = 99;
    private static final String TEST_GROUP_NAME = "group01";

    private static final long TEST_EXISTING_GROUP_ID = 1;

    private GroupRepository groupRepository = mock(GroupRepository.class);

    private Group group = Group
            .builder()
            .groupId(TEST_GROUP_ID)
            .name(TEST_GROUP_NAME)
            .build();

    @Test
    void shouldCreateGroupWhenEverythingIsRight() {

        when(groupRepository.save(group)).thenReturn(group);
        CreateGroupCmd sut = new CreateGroupCmd(group, groupRepository);
        CreateGroupCmdStatus status = callAddGroupCmdAs (TEST_USER_ID, sut);

        assertThat(status).isEqualTo(CreateGroupCmdStatus.OK);

    }

    @Test
    void shouldReturnGROUPALREADYEXISTSWhenGroupIdIsDuplicated () {
        Group group = Group
            .builder()
            .groupId(TEST_EXISTING_GROUP_ID)
            .name(TEST_GROUP_NAME)
            .build();

        when(groupRepository.countByGroupId(TEST_EXISTING_GROUP_ID)).thenReturn(1);

        CreateGroupCmd sut = new CreateGroupCmd(group, groupRepository);
        CreateGroupCmdStatus status = callAddGroupCmdAs (TEST_USER_ID, sut);

        assertThat(status).isEqualTo(CreateGroupCmdStatus.GROUP_ALREADY_EXISTS);
    }

    private CreateGroupCmdStatus callAddGroupCmdAs(String user, CreateGroupCmd sut){
        return Security.INSTANCE.runAs(new FakeSecurityContext(user, Collections.emptySet()), sut::run);
    }
}
