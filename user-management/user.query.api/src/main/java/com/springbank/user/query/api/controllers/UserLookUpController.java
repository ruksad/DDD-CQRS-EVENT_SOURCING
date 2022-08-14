package com.springbank.user.query.api.controllers;

import com.springbank.user.query.api.dto.UserLookUpResponse;
import com.springbank.user.query.api.queries.FindAllUsersQuery;
import com.springbank.user.query.api.queries.FindUserByIdQuery;
import com.springbank.user.query.api.queries.SearchUsersQuery;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/userLookUp")
@Slf4j
public class UserLookUpController {
    private final QueryGateway queryGateway;

    public UserLookUpController(QueryGateway queryGateway) {
        this.queryGateway = queryGateway;
    }

    @GetMapping(path = "/")
    @PreAuthorize("hasAuthority('READ_PRIVILEGE')")
    public ResponseEntity<UserLookUpResponse> getAllUsers() {
        try {
            var findAllUsersQuery = new FindAllUsersQuery();
            UserLookUpResponse response = queryGateway.query(findAllUsersQuery, ResponseTypes.instanceOf(UserLookUpResponse.class)).join();
            if(null== response || response.getUser()==null){
                return new ResponseEntity<>(null,HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            var safeErrorMessage = "Failed to complete get All users request";
            log.info("Exception : {}", e);
            return new ResponseEntity<>(new UserLookUpResponse(safeErrorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/filterById/{id}")
    @PreAuthorize("hasAuthority('READ_PRIVILEGE')")
    public ResponseEntity<UserLookUpResponse> getUserById(@PathVariable("id") String id) {
        try {
            var findByIdQuery = new FindUserByIdQuery(id);
            UserLookUpResponse response = queryGateway.query(findByIdQuery, ResponseTypes.instanceOf(UserLookUpResponse.class)).join();
            if(null== response || response.getUser()==null){
                return new ResponseEntity<>(null,HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            var safeErrorMessage = "Failed to complete get users by id request";
            log.info("Exception : {}", e);
            return new ResponseEntity<>(new UserLookUpResponse(safeErrorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/filterBySearch/{filter}")
    @PreAuthorize("hasAuthority('READ_PRIVILEGE')")
    public ResponseEntity<UserLookUpResponse> searchUserByFilter(@PathVariable("filter") String filter) {
        try {
            var searchQuery = new SearchUsersQuery(filter);
            UserLookUpResponse response = queryGateway.query(searchQuery, ResponseTypes.instanceOf(UserLookUpResponse.class)).join();
            if(null== response || response.getUser()==null){
                return new ResponseEntity<>(null,HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            var safeErrorMessage = "Failed to complete get users search request";
            log.info("Exception : {}", e);
            return new ResponseEntity<>(new UserLookUpResponse(safeErrorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
