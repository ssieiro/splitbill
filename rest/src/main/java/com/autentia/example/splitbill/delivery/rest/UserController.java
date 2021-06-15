package com.autentia.example.splitbill.delivery.rest;

import com.autentia.example.splitbill.delivery.rest.request.CreateUserRequest;
import com.autentia.example.splitbill.usecase.user.AddUserCmd;
import com.autentia.example.splitbill.usecase.user.AddUserCmdStatus;

import io.archimedes.usecase.UseCaseBus;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;

@Controller("/splitbill/users")
@Secured(SecurityRule.IS_AUTHENTICATED)
public class UserController {

    private UseCaseBus useCaseBus;

    public UserController(UseCaseBus useCaseBus){
        this.useCaseBus = useCaseBus;
    }

    @Post
    public HttpStatus createUser(@Body CreateUserRequest createUserRequest) {

        AddUserCmdStatus addUserCmdStatus = useCaseBus.invoke(new AddUserCmd(createUserRequest.toUser()));

        switch(addUserCmdStatus) {
            case OK:
                return HttpStatus.CREATED;
            case USER_ALREADY_EXISTS:
                return HttpStatus.BAD_REQUEST;
            case INTERNAL_ERROR:
            default:
                return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        
    }
}
