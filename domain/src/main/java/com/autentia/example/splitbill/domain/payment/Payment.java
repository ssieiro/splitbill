package com.autentia.example.splitbill.domain.payment;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

import com.autentia.example.splitbill.domain.usergroup.UserGroupID;

import io.micronaut.data.annotation.EmbeddedId;
import io.micronaut.data.annotation.MappedEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@MappedEntity("GROUP_PAYMENTS")
@EqualsAndHashCode
public class Payment{
    @EmbeddedId
    private UserGroupID id;
    @NotNull
    private LocalDateTime paymentTs;
    @Nullable
    private String description;
    @NotNull
    private BigDecimal amount;
    @Nullable
    private String payedTo;
}

