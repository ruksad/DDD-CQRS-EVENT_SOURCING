package com.scarycoders.lean.user.core.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Account {
    @Size(min = 2, message = "User name must be of minimum 2 characters")
    private String userName;
    @Size(min = 7, message = "Password should be at lease 7 chars long")
    private String password;
    @NotNull(message = "Specify at least one role")
    private List<Role> roles;
}
