package com.autentia.example.splitbill.usecase.group;

import com.autentia.example.splitbill.usecase.group.status.CreateGroupCmdStatus;
import com.autentia.example.splitbill.domain.group.Group;
import com.autentia.example.splitbill.persistence.group.GroupRepository;

import io.archimedes.locator.ServiceLocator;
import io.archimedes.usecase.Command;

public class CreateGroupCmd extends Command<CreateGroupCmdStatus>{
    private Group group;
    private GroupRepository groupRepository;

    public CreateGroupCmd(Group group, GroupRepository groupRepository){
        this.group = group;
        this.groupRepository = groupRepository;
    }

    public CreateGroupCmd(Group group) {
        this(group, ServiceLocator.INSTANCE.locate(GroupRepository.class));
    }

    @Override
    protected CreateGroupCmdStatus run(){
        if(groupRepository.countByGroupId(group.getGroupId())==1) {
            return CreateGroupCmdStatus.GROUP_ALREADY_EXISTS;
        }

        if(groupRepository.save(group).equals(group)){
            return CreateGroupCmdStatus.OK;
        }
        return CreateGroupCmdStatus.INTERNAL_ERROR;
    }
}

