package com.jjginga.AuthServiceApplication.service;

import com.jjginga.AuthServiceApplication.entity.MyUser;
import com.jjginga.AuthServiceApplication.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Primary
public class CustomUserService implements ReactiveUserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Lazy
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return userRepo.findByUsername(username)
                .switchIfEmpty(Mono.error(new UsernameNotFoundException("User not found with username: " + username)))
                .cast(UserDetails.class);
    }

    public Mono<MyUser> saveUser(MyUser user) {
    return userRepo.findByUsername(user.getUsername())
        .flatMap(existingUser -> Mono.<MyUser>error(new Exception("There is an account with that email address already.")))
        .switchIfEmpty(Mono.defer(() -> {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return userRepo.save(user);
        }));

    }



}

/**
package com.jjginga.AuthServiceApplication.service;

import com.jjginga.AuthServiceApplication.entity.MyUser;
import com.jjginga.AuthServiceApplication.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Lazy
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<MyUser> user = userRepo.findByUsername(username);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        return user.get();
    }

    public MyUser saveUser(MyUser user) throws Exception {
        if (userRepo.findByUsername(user.getUsername()).isPresent()) {
            throw new Exception("There is an account with that email address: " + user.getUsername());
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }
}
**/