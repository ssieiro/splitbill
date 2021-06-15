package com.autentia.example.splitbill.delivery.rest;

import com.autentia.example.splitbill.delivery.rest.request.AddUserToGroupRequest;
import com.autentia.example.splitbill.delivery.rest.request.CreateGroupRequest;
import com.autentia.example.splitbill.delivery.rest.response.GroupPaymentBalanceResponse;
import com.autentia.example.splitbill.usecase.group.AddUserToGroupCmd;
import com.autentia.example.splitbill.usecase.group.status.AddUserToGroupCmdStatus;
import com.autentia.example.splitbill.usecase.group.CreateGroupCmd;
import com.autentia.example.splitbill.usecase.group.status.CreateGroupCmdStatus;
import com.autentia.example.splitbill.usecase.balance.GroupPaymentsBalanceQry;
import com.autentia.example.splitbill.domain.balance.GroupPaymentBalance;

import io.archimedes.usecase.UseCaseBus;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;

@Controller("/splitbill/groups")
@Secured(SecurityRule.IS_ANONYMOUS)
public class GroupController{

    private UseCaseBus useCaseBus;

    public GroupController(UseCaseBus useCaseBus){
        this.useCaseBus = useCaseBus;
    }

    @Post
    public HttpStatus createGroup(@Body CreateGroupRequest createGroupRequest) {

        CreateGroupCmdStatus createGroupCmdStatus = useCaseBus.invoke(new CreateGroupCmd(createGroupRequest.toGroup()));

        switch(createGroupCmdStatus) {
            case OK:
                return HttpStatus.CREATED;
            case GROUP_ALREADY_EXISTS:
                return HttpStatus.BAD_REQUEST;
            case INTERNAL_ERROR:
            default:
                return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        
    }

    @Post("/{groupId}/users")
    public HttpStatus addUserToGroup (@Body AddUserToGroupRequest addUserToGroupRequest) {

        AddUserToGroupCmdStatus status = useCaseBus.invoke(new AddUserToGroupCmd(addUserToGroupRequest.getGroupId(), addUserToGroupRequest.getUserId()));

        switch(status) {
        case OK:
            return HttpStatus.CREATED;
        case USER_ALREADY_EXISTS_IN_GROUP:
            return HttpStatus.BAD_REQUEST;
        case INTERNAL_ERROR:
        default:
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }

    @Get("/{groupId}/balance")
    public GroupPaymentBalanceResponse getGroupBalance (long groupId) {
        GroupPaymentBalance groupPaymentBalance = useCaseBus.invoke(new GroupPaymentsBalanceQry(groupId));

        if(groupPaymentBalance != null) {
            return GroupPaymentBalanceResponse.of(groupPaymentBalance);
        } else {
            return new GroupPaymentBalanceResponse();
        }
    }
}
