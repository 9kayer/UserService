package com.blog.microservices.services;

import com.blog.microservices.domains.User;
import com.blog.microservices.dtos.user.UserParser;
import com.blog.microservices.dtos.user.UserRequest;
import com.blog.microservices.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.function.Function;

@Service
public class UserService {

    @Autowired
    private UserRepository repo;

    @Resource(name = "userParser")
    private UserParser userParser;

    public Flux<User> getUsers() {
        return repo.findAll();
    }

    public Mono<User> getUserById(String id) {
        return repo.findById(id);
    }

    public Mono<User> create(UserRequest user) {
        return repo.save(userParser.toModel(user));
    }

    public Mono<User> update(String id, UserRequest user){

        Function<Boolean,Mono<User>> function = ( item -> {
            if(item){
                User user1 = userParser.toModel(user);
                user1.setId(id);

                return repo.save(user1);
            }
            return Mono.empty();
        });

        return repo.existsById(id)
                 .flatMap(function);
    }

    public Mono<Void> delete(String id) {
        return repo.deleteById(id);
    }
}
