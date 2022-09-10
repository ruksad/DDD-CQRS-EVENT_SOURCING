package com.springbank.bankacc.query.api.handlers;

import com.springbank.bankacc.core.events.AccountClosedEvent;
import com.springbank.bankacc.core.events.AccountOpenedEvent;
import com.springbank.bankacc.core.events.FundsDepositedEvent;
import com.springbank.bankacc.core.events.FundsWithdrawnEvent;

public interface AccountEventHandler {
    void on(AccountOpenedEvent accountOpenedEvent);
    void on(FundsDepositedEvent fundsDepositedEvent);
    void on(FundsWithdrawnEvent fundsWithdrawnEvent);
    void on(AccountClosedEvent accountClosedEvent);
}
