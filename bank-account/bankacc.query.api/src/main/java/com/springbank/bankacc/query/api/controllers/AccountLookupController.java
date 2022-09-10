package com.springbank.bankacc.query.api.controllers;

import com.springbank.bankacc.query.api.dto.AccountLookupResponse;
import com.springbank.bankacc.query.api.dto.EqualityType;
import com.springbank.bankacc.query.api.queries.FindAccountByHolderId;
import com.springbank.bankacc.query.api.queries.FindAccountByIdQuery;
import com.springbank.bankacc.query.api.queries.FindAccountWithBalanceQuery;
import com.springbank.bankacc.query.api.queries.FindAllAccountsQuery;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/bankAccountLookup")
@Slf4j
public class AccountLookupController {

    private final QueryGateway queryGateway;

    @Autowired
    public AccountLookupController(QueryGateway queryGateway) {
        this.queryGateway = queryGateway;
    }

    @GetMapping(path = "/")
    @PreAuthorize("hasAuthority('READ_PRIVILEGE')")
    public ResponseEntity<AccountLookupResponse> getAllAccounts() {
        try {
            var findAll = new FindAllAccountsQuery();
            var response = queryGateway.query(findAll, ResponseTypes.instanceOf(AccountLookupResponse.class)).join();
            if (null == response || response.getAccountList() == null)
                return new ResponseEntity(null, HttpStatus.NO_CONTENT);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            var safeMessage = "Failed to complete get all accounts request";
            log.info(e.toString());
            return new ResponseEntity(new AccountLookupResponse(safeMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping(path = "/byId/{id}")
    @PreAuthorize("hasAuthority('READ_PRIVILEGE')")
    public ResponseEntity<AccountLookupResponse> getAccountsBydId(@PathVariable(value = "id") String id) {
        try {
            var query = new FindAccountByIdQuery(id);
            var response = queryGateway.query(query, ResponseTypes.instanceOf(AccountLookupResponse.class)).join();
            if (null == response || response.getAccountList() == null)
                return new ResponseEntity(response, HttpStatus.NO_CONTENT);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            var safeMessage = "Failed to complete get account by id";
            log.info(e.toString());
            return new ResponseEntity(new AccountLookupResponse(safeMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/byHolderId/{accountHolderId}")
    @PreAuthorize("hasAuthority('READ_PRIVILEGE')")
    public ResponseEntity<AccountLookupResponse> getAccountsBydHolderId(@PathVariable(value = "accountHolderId") String id) {
        try {
            var query = new FindAccountByHolderId(id);
            var response = queryGateway.query(query, ResponseTypes.instanceOf(AccountLookupResponse.class)).join();
            if (null == response || response.getAccountList() == null)
                return new ResponseEntity(null, HttpStatus.NO_CONTENT);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            var safeMessage = "Failed to complete get account by accountHolderId";
            log.info(e.toString());
            return new ResponseEntity(new AccountLookupResponse(safeMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/withBalance/{equalityType}/{balance}")
    @PreAuthorize("hasAuthority('READ_PRIVILEGE')")
    public ResponseEntity<AccountLookupResponse> getAccountsBydHolderId(@PathVariable(value = "equalityType") EqualityType equalityType,
                                                                        @PathVariable(value = "balance") double balance) {
        try {
            var query = new FindAccountWithBalanceQuery(equalityType,balance);
            var response = queryGateway.query(query, ResponseTypes.instanceOf(AccountLookupResponse.class)).join();
            if (null == response || response.getAccountList() == null)
                return new ResponseEntity(null, HttpStatus.NO_CONTENT);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            var safeMessage = "Failed to complete get account by balance query";
            log.info(e.toString());
            return new ResponseEntity(new AccountLookupResponse(safeMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
