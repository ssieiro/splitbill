package com.autentia.example.splitbill.delivery.rest.request;

import javax.validation.constraints.NotNull;

import com.autentia.example.splitbill.domain.group.Group;
import com.autentia.example.splitbill.domain.user.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateGroupRequest{
    @NotNull
    private long groupId;
    @NotNull
    private String groupName;

    public Group toGroup() {
        return Group.builder()
                .groupId(this.groupId)
                .name(this.groupName)
                .build();
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateUserRequest {
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
}
