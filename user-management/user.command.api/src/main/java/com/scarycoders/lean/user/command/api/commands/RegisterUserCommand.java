package com.scarycoders.lean.user.command.api.commands;

import com.scarycoders.lean.user.core.models.User;
import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class RegisterUserCommand {

    @TargetAggregateIdentifier
    private String id;

    @NotNull(message = "No user details were supplied")
    private User user;
}
