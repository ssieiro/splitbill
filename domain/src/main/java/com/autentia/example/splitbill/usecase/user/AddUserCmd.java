package com.autentia.example.splitbill.usecase.user;

import com.autentia.example.splitbill.domain.user.User;
import com.autentia.example.splitbill.persistence.user.UserRepository;

import io.archimedes.locator.ServiceLocator;
import io.archimedes.usecase.Command;

public class AddUserCmd extends Command<AddUserCmdStatus>{
    private User user;
    private UserRepository userRepository;

    public AddUserCmd(User user, UserRepository userRepository){
        this.user = user;
        this.userRepository = userRepository;
    }

    public AddUserCmd(User user) {
        this(user, ServiceLocator.INSTANCE.locate(UserRepository.class));
    }

    @Override
    protected AddUserCmdStatus run(){
        if(userRepository.existsByUserId(user.getUserId()) ) {
            return AddUserCmdStatus.USER_ALREADY_EXISTS;
        }

        userRepository.save(user);

        return AddUserCmdStatus.OK;

    }
}

