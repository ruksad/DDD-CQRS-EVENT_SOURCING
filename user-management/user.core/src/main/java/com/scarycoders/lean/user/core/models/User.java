package com.scarycoders.lean.user.core.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collation = "users")
public class User {

    @Id
    private String id;
    @NotEmpty(message= "firstname is mandatory")
    private String firstName;
    private String lastName;
    @Email(message = "Please provide a valid mail")
    private String email;
    @NotNull(message = "Please provide account credentials")
    @Valid
    private Account account;
}

