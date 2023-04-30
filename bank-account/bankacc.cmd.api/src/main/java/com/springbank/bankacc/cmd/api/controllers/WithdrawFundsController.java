package com.springbank.bankacc.cmd.api.controllers;

import com.springbank.bankacc.cmd.api.commands.WithdrawFundsCommand;
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
@Slf4j
@RequestMapping(path = "/api/v1/withdrawFunds")
public class WithdrawFundsController {

    private final CommandGateway commandGateway;

    @Autowired
    public WithdrawFundsController(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @PutMapping(path = "/{id}")
    @PreAuthorize("hasAuthority('WRITE_PRIVILEGE')")
    public ResponseEntity<BaseResponse> withdrawFunds(@PathVariable("id") String bankAccountId,
                                                      @Valid @RequestBody WithdrawFundsCommand withdrawFundsCommand){
        withdrawFundsCommand.setId(bankAccountId);
        log.info("WithdrawFundsCommand command is received with bank account id {}", bankAccountId);
        try {
            commandGateway.sendAndWait(withdrawFundsCommand);
            return new ResponseEntity<>(new BaseResponse("Funds withdrawn successfully"), HttpStatus.OK);
        } catch (Exception e) {
            var safeErrorMessage = " Error while processing request to withdraw funds from bank account for id= " + bankAccountId;
            log.info("Exception : {}", e);
            return new ResponseEntity<>(new OpenAccountResponse(bankAccountId, safeErrorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
