package com.springbank.bankacc.query.api.handlers;

import com.springbank.bankacc.query.api.dto.AccountLookupResponse;
import com.springbank.bankacc.query.api.queries.FindAccountByHolderId;
import com.springbank.bankacc.query.api.queries.FindAccountByIdQuery;
import com.springbank.bankacc.query.api.queries.FindAccountWithBalanceQuery;
import com.springbank.bankacc.query.api.queries.FindAllAccountsQuery;

public interface AccountQueryHandler {
    AccountLookupResponse findAccountBydId(FindAccountByIdQuery id);
    AccountLookupResponse findAccountByHolderId(FindAccountByHolderId id);
    AccountLookupResponse findAllAccounts(FindAllAccountsQuery query);
    AccountLookupResponse findAccountsWithBalance(FindAccountWithBalanceQuery query);
}
