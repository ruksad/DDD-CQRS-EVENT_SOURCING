package com.scarycoders.lean.user.core.events;

import com.scarycoders.lean.user.core.models.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRegisteredEvent {
    private String id;
    private User user;
}
