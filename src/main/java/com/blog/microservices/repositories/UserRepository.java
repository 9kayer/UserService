package com.blog.microservices.repositories;

import com.blog.microservices.domains.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface UserRepository extends ReactiveCrudRepository<User, String> {
}
