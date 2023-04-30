package com.springbank.bankacc.cmd.api.aggregates;

import com.springbank.bankacc.cmd.api.commands.CloseAccountCommand;
import com.springbank.bankacc.cmd.api.commands.DepositFundsCommand;
import com.springbank.bankacc.cmd.api.commands.OpenAccountCommand;
import com.springbank.bankacc.cmd.api.commands.WithdrawFundsCommand;
import com.springbank.bankacc.core.events.AccountClosedEvent;
import com.springbank.bankacc.core.events.AccountOpenedEvent;
import com.springbank.bankacc.core.events.FundsDepositedEvent;
import com.springbank.bankacc.core.events.FundsWithdrawnEvent;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import java.util.Date;

@Aggregate
@NoArgsConstructor
@Slf4j
public class AccountAggregate {
    @AggregateIdentifier
    private String id;
    private String accountHolderId;
    private double balance;

    @CommandHandler
    public AccountAggregate(OpenAccountCommand openAccountCommand) {
        var event = AccountOpenedEvent.builder()
                .id(openAccountCommand.getId())
                .accountHolderId(openAccountCommand.getAccountHolderId())
                .accountType(openAccountCommand.getAccountType())
                .creationDate(new Date())
                .openingBalance(openAccountCommand.getOpeningBalance())
                .build();
        log.info("Account opened event is received for id: {}", openAccountCommand.getId());
        AggregateLifecycle.apply(event);
    }

    @EventSourcingHandler
    public void on(AccountOpenedEvent accountOpenedEvent) {
        log.info("Account aggregate is created for id: {}", accountOpenedEvent.getId());
        this.id = accountOpenedEvent.getId();
        this.accountHolderId = accountOpenedEvent.getAccountHolderId();
        this.balance = accountOpenedEvent.getOpeningBalance();
    }

    @CommandHandler
    public void handle(DepositFundsCommand depositFundsCommand) {
        var event = FundsDepositedEvent.builder()
                .id(depositFundsCommand.getId())
                .amount(depositFundsCommand.getAmount())
                .balance(this.balance + depositFundsCommand.getAmount())
                .build();
        AggregateLifecycle.apply(event);
    }

    @EventSourcingHandler
    public void on(FundsDepositedEvent fundsDepositedEvent) {
        this.balance += fundsDepositedEvent.getAmount();
    }

    @CommandHandler
    public void handle(WithdrawFundsCommand withdrawFundsCommand) {
        log.info("FundsWithdrawnEvent event will be triggered with account id {}", withdrawFundsCommand.getId());
        var amount = withdrawFundsCommand.getAmount();
        if (this.balance - amount < 0) {
            throw new IllegalStateException("Withdrawal declined, Insufficient funds");
        }
        var event = FundsWithdrawnEvent.builder()
                .id(withdrawFundsCommand.getId())
                .amount(amount)
                .balance(this.balance - amount)
                .build();
        AggregateLifecycle.apply(event);
    }

    @EventSourcingHandler
    public void on(FundsWithdrawnEvent fundsWithdrawnEvent) {

        log.info("FundsWithdrawnEvent will reduce fund from {} by {} units of money",
                fundsWithdrawnEvent.getId(), fundsWithdrawnEvent.getAmount());
        this.balance -= fundsWithdrawnEvent.getAmount();
    }

    @CommandHandler
    public void handle(CloseAccountCommand closeAccountCommand) {
        var event = AccountClosedEvent.builder()
                .id(closeAccountCommand.getId())
                .build();
        AggregateLifecycle.apply(event);
    }

    @EventSourcingHandler
    public void on(AccountClosedEvent accountClosedEvent) {

        AggregateLifecycle.markDeleted();
    }
}
