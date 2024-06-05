package com.jjginga.AuthServiceApplication.repo;

import com.jjginga.AuthServiceApplication.entity.MyUser;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Repository
public interface UserRepo extends ReactiveMongoRepository<MyUser, String> {

    Mono<MyUser> findByUsername(String username);

}
