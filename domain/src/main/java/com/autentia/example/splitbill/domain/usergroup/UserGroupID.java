package com.autentia.example.splitbill.domain.usergroup;

import io.micronaut.data.annotation.Embeddable;
import io.micronaut.data.annotation.MappedProperty;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@Embeddable
@EqualsAndHashCode
public class UserGroupID {
    @MappedProperty("GROUP_ID")
    private final long groupId;
    @MappedProperty("USER_ID")
    private final String userId;
}
