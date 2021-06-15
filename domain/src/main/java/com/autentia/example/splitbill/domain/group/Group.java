package com.autentia.example.splitbill.domain.group;

import javax.validation.constraints.NotNull;

import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.MappedProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@MappedEntity("GROUPS")
public class Group{
    @Id
    private long groupId;
    @NotNull
    @MappedProperty("GROUP_NAME")
    private String name;
}
