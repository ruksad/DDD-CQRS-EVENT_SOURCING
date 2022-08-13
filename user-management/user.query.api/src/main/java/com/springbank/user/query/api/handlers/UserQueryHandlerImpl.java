package com.springbank.user.query.api.handlers;

import com.springbank.user.core.models.User;
import com.springbank.user.query.api.dto.UserLookUpResponse;
import com.springbank.user.query.api.queries.FindAllUsersQuery;
import com.springbank.user.query.api.queries.FindUserByIdQuery;
import com.springbank.user.query.api.queries.SearchUsersQuery;
import com.springbank.user.query.api.repositories.UserRepository;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserQueryHandlerImpl implements UserQueryHandler {

    private final UserRepository userRepository;

    @Autowired
    public UserQueryHandlerImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @QueryHandler
    public UserLookUpResponse getUserById(FindUserByIdQuery userByIdQuery) {
        Optional<User> byId = userRepository.findById(userByIdQuery.getId());
        return byId.isPresent()? new UserLookUpResponse(byId.get()):null;
    }

    @Override
    @QueryHandler
    public UserLookUpResponse searchUsers(SearchUsersQuery searchUsersQuery) {

        List<User> byFilterRegex = userRepository.findByFilterRegex(searchUsersQuery.getSearchFilter());
        return new UserLookUpResponse(byFilterRegex);
    }

    @Override
    @QueryHandler
    public UserLookUpResponse getAllUsers(FindAllUsersQuery findAllUsersQuery) {
        var users = userRepository.findAll();
        return new UserLookUpResponse(users);
    }
}
