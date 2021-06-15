package com.autentia.example.splitbill.domain.usergroup;

import io.micronaut.data.annotation.EmbeddedId;
import io.micronaut.data.annotation.MappedEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@MappedEntity("USERS_GROUPS")
@EqualsAndHashCode
public class UserGroup{
    @EmbeddedId
    private UserGroupID userGroupID;
}
