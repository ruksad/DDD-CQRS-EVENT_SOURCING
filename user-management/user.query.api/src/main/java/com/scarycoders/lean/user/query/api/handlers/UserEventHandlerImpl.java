package com.scarycoders.lean.user.query.api.handlers;

import com.scarycoders.lean.user.core.events.UserRegisteredEvent;
import com.scarycoders.lean.user.core.events.UserRemovedEvent;
import com.scarycoders.lean.user.core.events.UserUpdatedEvent;
import com.scarycoders.lean.user.query.api.repositories.UserRepository;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@ProcessingGroup("user-group") // same as consumer group, axon tracks the offset as from where axon will consume new packet
public class UserEventHandlerImpl implements UserEventHandler {

    private final UserRepository userRepository;

    @Autowired
    public UserEventHandlerImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @EventHandler
    public void on(UserRegisteredEvent userRegisteredEvent) {
        userRepository.save(userRegisteredEvent.getUser());
    }

    @Override
    @EventHandler
    public void on(UserUpdatedEvent userUpdatedEvent) {
        userRepository.save(userUpdatedEvent.getUser());
    }

    @Override
    @EventHandler
    public void on(UserRemovedEvent userRemovedEvent) {
        userRepository.deleteById(userRemovedEvent.getId());
    }
}
