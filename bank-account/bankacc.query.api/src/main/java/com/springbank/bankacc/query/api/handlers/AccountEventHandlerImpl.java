package com.springbank.bankacc.query.api.handlers;

import com.springbank.bankacc.core.events.AccountClosedEvent;
import com.springbank.bankacc.core.events.AccountOpenedEvent;
import com.springbank.bankacc.core.events.FundsDepositedEvent;
import com.springbank.bankacc.core.events.FundsWithdrawnEvent;
import com.springbank.bankacc.core.models.BankAccount;
import com.springbank.bankacc.query.api.repositories.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@ProcessingGroup("bankAccount-group")
@Slf4j
public class AccountEventHandlerImpl implements AccountEventHandler {

    private final AccountRepository accRepo;

    @Autowired
    public AccountEventHandlerImpl(AccountRepository accRepo) {
        this.accRepo = accRepo;
    }


    @Override
    @EventHandler
    public void on(AccountOpenedEvent accountOpenedEvent) {
        log.info("AccountOpenedEvent is received on query side with id {}", accountOpenedEvent.getId());
        var bankAccount = BankAccount.builder()
                .id(accountOpenedEvent.getId())
                .accountHolderId(accountOpenedEvent.getAccountHolderId())
                .accountType(accountOpenedEvent.getAccountType())
                .balance(accountOpenedEvent.getOpeningBalance())
                .creationDate(accountOpenedEvent.getCreationDate())
                .build();
        accRepo.save(bankAccount);
    }

    @Override
    @EventHandler
    public void on(FundsDepositedEvent fundsDepositedEvent) {
        Optional<BankAccount> byId = accRepo.findById(fundsDepositedEvent.getId());
        if (byId.isPresent()) {
            byId.get().setBalance(fundsDepositedEvent.getBalance());
            accRepo.save(byId.get());
        }
    }

    @Override
    @EventHandler
    public void on(FundsWithdrawnEvent fundsWithdrawnEvent) {
        log.info("FundsWithdrawnEvent is received on query side and funds will be deducted by {} units and in account id {} ", fundsWithdrawnEvent.getAmount(), fundsWithdrawnEvent.getId());
        Optional<BankAccount> byId = accRepo.findById(fundsWithdrawnEvent.getId());
        if (byId.isPresent()) {
            byId.get().setBalance(fundsWithdrawnEvent.getBalance());
            accRepo.save(byId.get());
        }
    }

    @Override
    @EventHandler
    public void on(AccountClosedEvent accountClosedEvent) {
        accRepo.deleteById(accountClosedEvent.getId());
    }
}
