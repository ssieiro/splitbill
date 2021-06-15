package com.autentia.example.splitbill.delivery.rest.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddUserToGroupRequest{
    private long groupId;
    private String userId;
}
