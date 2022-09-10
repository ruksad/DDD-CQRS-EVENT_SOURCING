package com.springbank.bankacc.query.api.handlers;

import com.springbank.bankacc.core.models.BankAccount;
import com.springbank.bankacc.query.api.dto.AccountLookupResponse;
import com.springbank.bankacc.query.api.dto.EqualityType;
import com.springbank.bankacc.query.api.queries.FindAccountByHolderId;
import com.springbank.bankacc.query.api.queries.FindAccountByIdQuery;
import com.springbank.bankacc.query.api.queries.FindAccountWithBalanceQuery;
import com.springbank.bankacc.query.api.queries.FindAllAccountsQuery;
import com.springbank.bankacc.query.api.repositories.AccountRepository;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class AccountsQueryHandlerImpl implements AccountQueryHandler {

    private final AccountRepository repo;

    @Autowired
    AccountsQueryHandlerImpl(AccountRepository repo) {
        this.repo = repo;
    }

    @Override
    @QueryHandler
    public AccountLookupResponse findAccountBydId(FindAccountByIdQuery id) {
        Optional<BankAccount> byId = repo.findById(id.getId());
        return byId.isPresent() ? new AccountLookupResponse("Bank account successfully returned", byId.get()) :
                new AccountLookupResponse("No account found by id : " + id.getId());
    }

    @Override
    @QueryHandler
    public AccountLookupResponse findAccountByHolderId(FindAccountByHolderId id) {
        Optional<BankAccount> byId = repo.findByAccountHolderId(id.getAccountHolderId());
        return byId.isPresent() ? new AccountLookupResponse("Bank account successfully returned", byId.get()) :
                new AccountLookupResponse("No account found by account holder id : " + id.getAccountHolderId());
    }

    @Override
    @QueryHandler
    public AccountLookupResponse findAllAccounts(FindAllAccountsQuery query) {
        Iterable<BankAccount> all = repo.findAll();
        if (!all.iterator().hasNext())
            return new AccountLookupResponse("No bank accounts found");
        var allAccounts = new ArrayList<BankAccount>();
        all.forEach(x -> allAccounts.add(x));
        return new AccountLookupResponse(String.format("Successfully returned %s records", allAccounts.size()), allAccounts);
    }

    @Override
    @QueryHandler
    public AccountLookupResponse findAccountsWithBalance(FindAccountWithBalanceQuery query) {
        var balanceList = query.getEqualityType() == EqualityType.LESS_THAN ? repo.findByBalanceLessThan(query.getBalance()) : repo.findByBalanceGreaterThan(query.getBalance());
        var resp = balanceList.isPresent() ?
                new AccountLookupResponse(String.format("Total %s bank account successfully returned", balanceList.get().size()), balanceList.get()) :
                new AccountLookupResponse("No bank accounts found found");
        return resp;
    }
}
