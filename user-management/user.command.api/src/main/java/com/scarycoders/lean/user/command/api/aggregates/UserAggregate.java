package com.scarycoders.lean.user.command.api.aggregates;

import com.scarycoders.lean.user.command.api.commands.RegisterUserCommand;
import com.scarycoders.lean.user.command.api.commands.RemoveUserCommand;
import com.scarycoders.lean.user.command.api.commands.UpdateUserCommand;
import com.scarycoders.lean.user.command.api.security.PasswordEncoder;
import com.scarycoders.lean.user.command.api.security.PasswordEncoderImpl;
import com.scarycoders.lean.user.core.events.UserRegisteredEvent;
import com.scarycoders.lean.user.core.events.UserRemovedEvent;
import com.scarycoders.lean.user.core.events.UserUpdatedEvent;
import com.scarycoders.lean.user.core.models.User;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import java.util.UUID;

@Aggregate
public class UserAggregate {
    @AggregateIdentifier
    private String id;
    private User user;
    private final PasswordEncoder passwordEncoder;

    public UserAggregate() {
        passwordEncoder = new PasswordEncoderImpl();
    }

    @CommandHandler
    public UserAggregate(RegisterUserCommand registerUserCommand) {
        var newUser = registerUserCommand.getUser();
        newUser.setId(registerUserCommand.getId());
        var password = newUser.getAccount().getPassword();
        passwordEncoder = new PasswordEncoderImpl();
        var hashedPassword = passwordEncoder.hashPassword(password);
        newUser.getAccount().setPassword(hashedPassword);

        var event = UserRegisteredEvent.builder().id(registerUserCommand.getId()).user(newUser).build();
        AggregateLifecycle.apply(event);
    }

    @CommandHandler
    public void handle(UpdateUserCommand updateUserCommand) {
        var updatedUser = updateUserCommand.getUser();
        updatedUser.setId(updateUserCommand.getId());
        var password = updatedUser.getAccount().getPassword();
        var hashedPassword = passwordEncoder.hashPassword(password);
        updatedUser.getAccount().setPassword(hashedPassword);

        var userUpdatedEvent = UserUpdatedEvent.builder().id(UUID.randomUUID().toString()).user(updatedUser).build();
        AggregateLifecycle.apply(userUpdatedEvent);
    }

    @CommandHandler
    public void handle(RemoveUserCommand removeUserCommand) {
        var removedUserEvent = new UserRemovedEvent();
        removedUserEvent.setId(removeUserCommand.getId());
        AggregateLifecycle.apply(removedUserEvent);
    }

    @EventSourcingHandler
    public void on(UserRegisteredEvent userRegisteredEvent) {
        this.id = userRegisteredEvent.getId();
        this.user = userRegisteredEvent.getUser();
    }

    @EventSourcingHandler
    public void on(UserUpdatedEvent userUpdatedEvent) {

        this.user = userUpdatedEvent.getUser();
    }

    @EventSourcingHandler
    public void on(UserRemovedEvent userRemovedEvent) {

        AggregateLifecycle.markDeleted();
    }

}
