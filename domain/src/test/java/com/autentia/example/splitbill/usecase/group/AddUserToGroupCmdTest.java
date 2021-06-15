package com.autentia.example.splitbill.usecase.group;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;

import org.junit.jupiter.api.Test;

import com.autentia.example.splitbill.usecase.group.status.AddUserToGroupCmdStatus;
import com.autentia.example.splitbill.persistence.group.GroupRepository;
import com.autentia.example.splitbill.domain.usergroup.UserGroup;
import com.autentia.example.splitbill.domain.usergroup.UserGroupID;
import com.autentia.example.splitbill.persistence.usergroup.UserGroupRepository;

import io.archimedes.security.Security;
import io.archimedes.security.test.FakeSecurityContext;

public class AddUserToGroupCmdTest{

    private static final String TEST_USER_ID = "carlos";
    private static final long TEST_GROUP_ID = 99;
    private static final String TEST_GROUP_NAME = "group01";

    private static final long TEST_EXISTING_GROUP_ID = 1;
    
    private GroupRepository groupRepository = mock(GroupRepository.class);
    private UserGroupRepository userGroupRepository = mock(UserGroupRepository.class);

    @Test
    void shouldAddUserToGroupWhenEverythingIsRight() {

        UserGroup userGroup = UserGroup.builder().userGroupID(new UserGroupID(TEST_GROUP_ID, TEST_USER_ID)).build();
        when(userGroupRepository.save(any(UserGroup.class))).thenReturn(userGroup);
        AddUserToGroupCmd sut = new AddUserToGroupCmd(TEST_GROUP_ID, TEST_USER_ID, userGroupRepository);
        AddUserToGroupCmdStatus status = callAddUserToGroupCmdAs (TEST_USER_ID, sut);

        assertThat(status).isEqualTo(AddUserToGroupCmdStatus.OK);

    }

    @Test
    void shouldReturnUSERALREADYEXISTSINGROUPWhenUserIsYetInGroup () {
        UserGroup userGroup = UserGroup.builder().userGroupID(new UserGroupID(TEST_GROUP_ID, TEST_USER_ID)).build();
        when(userGroupRepository.existsByUserGroupID(new UserGroupID(TEST_GROUP_ID, TEST_USER_ID))).thenReturn(true);

        AddUserToGroupCmd sut = new AddUserToGroupCmd(TEST_GROUP_ID, TEST_USER_ID, userGroupRepository);
        AddUserToGroupCmdStatus status = callAddUserToGroupCmdAs (TEST_USER_ID, sut);

        assertThat(status).isEqualTo(AddUserToGroupCmdStatus.USER_ALREADY_EXISTS_IN_GROUP);
    }


    private AddUserToGroupCmdStatus callAddUserToGroupCmdAs(String user, AddUserToGroupCmd sut){
        return Security.INSTANCE.runAs(new FakeSecurityContext(user, Collections.emptySet()), sut::run);
    }
}
