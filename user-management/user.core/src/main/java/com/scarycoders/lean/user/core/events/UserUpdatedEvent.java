package com.scarycoders.lean.user.core.events;


import com.scarycoders.lean.user.core.models.User;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserUpdatedEvent {
    private String id;
    private User user;
}
