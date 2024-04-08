package com.github.hal4j.spring.example;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class UserRepository {

    private final List<User> users = List.of(
            new User(UUID.randomUUID(), 1, "mary"),
            new User(UUID.randomUUID(), 2, "vivek"),
            new User(UUID.randomUUID(), 1, "frida"),
            new User(UUID.randomUUID(), 2, "konstantin"),
            new User(UUID.randomUUID(), 5, "jasmin")
    );

    public List<User> findAll() {
        return users;
    }

    public List<User> findAllByVersion(int version) {
        return users.stream().filter(user -> user.version() == version).toList();
    }

    public Optional<User> findById(UUID uuid) {
        return users.stream().filter(user -> user.uuid().equals(uuid)).findFirst();
    }

}
