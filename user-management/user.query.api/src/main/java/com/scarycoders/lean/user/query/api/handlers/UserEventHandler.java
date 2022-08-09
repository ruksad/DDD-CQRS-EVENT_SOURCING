package com.scarycoders.lean.user.query.api.handlers;

import com.scarycoders.lean.user.core.events.UserRegisteredEvent;
import com.scarycoders.lean.user.core.events.UserRemovedEvent;
import com.scarycoders.lean.user.core.events.UserUpdatedEvent;

public interface UserEventHandler {
    void on(UserRegisteredEvent userRegisteredEvent);
    void on(UserUpdatedEvent userUpdatedEvent);
    void on(UserRemovedEvent userRemovedEvent);
}
