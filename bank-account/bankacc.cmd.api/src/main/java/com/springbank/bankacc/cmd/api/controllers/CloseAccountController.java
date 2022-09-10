package com.springbank.bankacc.cmd.api.controllers;

import com.springbank.bankacc.cmd.api.commands.CloseAccountCommand;
import com.springbank.bankacc.cmd.api.dto.OpenAccountResponse;
import com.springbank.bankacc.core.dto.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/closeAccount")
@Slf4j
public class CloseAccountController {

    private final CommandGateway commandGateway;

    @Autowired
    public CloseAccountController(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @DeleteMapping(path = "/{id}")
    @PreAuthorize("hasAuthority('WRITE_PRIVILEGE')")
    public ResponseEntity<BaseResponse> closeBankAccount(@PathVariable(value = "id") String bankAccountId) {

        var deleteAccount = CloseAccountCommand.builder().id(bankAccountId).build();

        try {
            commandGateway.send(deleteAccount);
            return new ResponseEntity<>(new BaseResponse("Bank account successfully closed"), HttpStatus.OK);
        } catch (Exception e) {
            var safeErrorMessage = " Error while closing bank account for id= " + bankAccountId;
            log.info(e.toString());
            return new ResponseEntity<>(new OpenAccountResponse(bankAccountId, safeErrorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
