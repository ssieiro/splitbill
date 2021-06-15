package com.autentia.example.splitbill.domain.user;

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
@MappedEntity ( value = "USERS")
public class User{
    @Id
    private String userId;
    @NotNull
    @MappedProperty ( value = "USER_NAME")
    private String name;
    @NotNull
    @MappedProperty ( value = "USER_PWD")
    private String password;
}
