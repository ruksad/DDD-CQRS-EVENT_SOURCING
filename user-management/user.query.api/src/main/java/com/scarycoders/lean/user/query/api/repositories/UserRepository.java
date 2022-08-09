package com.scarycoders.lean.user.query.api.repositories;

import com.scarycoders.lean.user.core.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User,String> {
}
