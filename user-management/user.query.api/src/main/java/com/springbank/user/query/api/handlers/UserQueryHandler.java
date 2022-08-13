package com.springbank.user.query.api.handlers;

import com.springbank.user.query.api.dto.UserLookUpResponse;
import com.springbank.user.query.api.queries.FindAllUsersQuery;
import com.springbank.user.query.api.queries.FindUserByIdQuery;
import com.springbank.user.query.api.queries.SearchUsersQuery;

public interface UserQueryHandler {
    UserLookUpResponse getUserById(FindUserByIdQuery userByIdQuery);
    UserLookUpResponse searchUsers(SearchUsersQuery searchUsersQuery);
    UserLookUpResponse getAllUsers(FindAllUsersQuery findAllUsersQuery);
}
