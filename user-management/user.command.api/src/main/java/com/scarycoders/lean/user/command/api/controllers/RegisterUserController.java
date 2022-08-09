package com.scarycoders.lean.user.command.api.controllers;

import com.scarycoders.lean.user.command.api.commands.RegisterUserCommand;
import com.scarycoders.lean.user.command.api.dto.RegisterUserResponse;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/registerUser")
@Slf4j
public class RegisterUserController {

    private final CommandGateway commandGateway;

    @Autowired
    public RegisterUserController(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @PostMapping
    public ResponseEntity<RegisterUserResponse> registerUser(@Valid  @RequestBody RegisterUserCommand registerUserCommand){
        try{
            registerUserCommand.setId(UUID.randomUUID().toString());
            commandGateway.send(registerUserCommand);
            return new ResponseEntity<>(new RegisterUserResponse("User successfully registered"),HttpStatus.CREATED);
        }catch (Exception e){
            var safeErrorMessage= "Error while processing register user request for id= "+ registerUserCommand.getId();
            log.info(e.toString());
            return new ResponseEntity<>(new RegisterUserResponse(safeErrorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
