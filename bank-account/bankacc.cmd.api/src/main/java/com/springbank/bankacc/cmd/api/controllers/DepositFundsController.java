package com.springbank.bankacc.cmd.api.controllers;

import com.springbank.bankacc.cmd.api.commands.DepositFundsCommand;
import com.springbank.bankacc.cmd.api.dto.OpenAccountResponse;
import com.springbank.bankacc.core.dto.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/api/v1/depositFunds")
@Slf4j
public class DepositFundsController {
    private final CommandGateway commandGateway;

    @Autowired
    public DepositFundsController(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @PutMapping(path = "/{id}")
    @PreAuthorize("hasAuthority('WRITE_PRIVILEGE')")
    public ResponseEntity<BaseResponse> depositFunds(@PathVariable(value = "id") String bankAccountId, @Valid @RequestBody DepositFundsCommand depositFundsCommand) {

        depositFundsCommand.setId(bankAccountId);

        try {
            commandGateway.send(depositFundsCommand);
            return new ResponseEntity<>(new BaseResponse("Funds deposited successfully deposited"), HttpStatus.OK);
        } catch (Exception e) {
            var safeErrorMessage = " Error while processing request to deposit funds bank account for id= " + bankAccountId;
            log.info(e.toString());
            return new ResponseEntity<>(new OpenAccountResponse(bankAccountId, safeErrorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
