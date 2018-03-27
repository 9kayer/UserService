package com.blog.microservices.controllers;

import com.blog.microservices.dtos.user.UserParser;
import com.blog.microservices.dtos.user.UserRequest;
import com.blog.microservices.dtos.user.UserResponse;
import com.blog.microservices.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${root}")
public class UserController {

    @Autowired
    private UserService service;

    @Resource(name = "userParser")
    private UserParser userParser;

    @GetMapping()
    public Mono<ResponseEntity<Set<UserResponse>>> getUsers() {
        return service.getUsers()
                .collectList()
                .flatMap(list -> Mono.just( ResponseEntity.ok( new HashSet<>(list.stream()
                                                                    .map(user -> userParser.toDTO(user))
                                                                    .collect(Collectors.toList()))))
                );
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<UserResponse>> getUser(@PathVariable String id) {
        return service.getUserById(id).map( user -> ResponseEntity.ok(userParser.toDTO(user)) );
    }

    @PostMapping
    public Mono<ResponseEntity<UserResponse>> create(@RequestBody UserRequest userRequest) {
        return service.create(userRequest).map(user -> ResponseEntity.ok(userParser.toDTO(user)));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<UserResponse>> update(@PathVariable String id,@RequestBody UserRequest userRequest) {
        return service.update(id,userRequest).map(user -> ResponseEntity.ok(userParser.toDTO(user)));
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity> delete(@PathVariable String id) {
        return service.delete(id).map(item -> ResponseEntity.ok(item));
    }

}
