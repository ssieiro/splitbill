package com.autentia.example.splitbill.delivery.rest.request;

import javax.validation.constraints.NotNull;

import com.autentia.example.splitbill.domain.user.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequest {
    @NotNull
    private String userId;
    @NotNull
    private String userName;
    @NotNull
    private String userPwd;

    public User toUser() {
        return User.builder()
                .userId(this.userId)
                .name(this.userName)
                .password(this.userPwd)
                .build();
    }
}
