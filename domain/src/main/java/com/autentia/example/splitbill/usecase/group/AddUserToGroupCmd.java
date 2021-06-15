package com.autentia.example.splitbill.usecase.group;

import com.autentia.example.splitbill.usecase.group.status.AddUserToGroupCmdStatus;
import com.autentia.example.splitbill.domain.usergroup.UserGroup;
import com.autentia.example.splitbill.domain.usergroup.UserGroupID;
import com.autentia.example.splitbill.persistence.usergroup.UserGroupRepository;

import io.archimedes.locator.ServiceLocator;
import io.archimedes.usecase.Command;

public class AddUserToGroupCmd extends Command<AddUserToGroupCmdStatus>{

    private long groupId;
    private String userId;
    private UserGroupRepository userGroupRepository;

    public AddUserToGroupCmd(long groupId, String userId, UserGroupRepository userGroupRepository){
        this.groupId = groupId;
        this.userId = userId;
        this.userGroupRepository = userGroupRepository;
    }

    public AddUserToGroupCmd(long groupId, String userId) {
        this(groupId, userId, ServiceLocator.INSTANCE.locate(UserGroupRepository.class));
    }

    @Override
    protected AddUserToGroupCmdStatus run(){

        if(userGroupRepository.existsByUserGroupID(new UserGroupID(groupId, userId))) {
            return AddUserToGroupCmdStatus.USER_ALREADY_EXISTS_IN_GROUP;
        }

        userGroupRepository.save(UserGroup.builder().userGroupID(new UserGroupID(groupId, userId)).build());

        return AddUserToGroupCmdStatus.OK;
    }
}

