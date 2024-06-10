package com.jjginga.AuthServiceApplication.repo;

import com.jjginga.AuthServiceApplication.entity.MyUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends MongoRepository<MyUser, String> {

    Optional<MyUser> findByUsername(String username);

}
