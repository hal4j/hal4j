package com.github.hal4j.resources.test.model;

import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.UUID;

public class UserProfile {

    private final String name;

    private final String email;

    private final URI userpic;

    private final UUID uuid;

    public static final UserProfile someUserProfile() {
        return new UserProfile(Any.name(), Any.email(), Any.pictureLink());
    }

    public UserProfile(String name, String email, URI userpic) {
        this.uuid = UUID.randomUUID();
        this.name = name;
        this.email = email;
        this.userpic = userpic;
    }

    public UUID uuid() {
        return uuid;
    }

    public String name() {
        return name;
    }

    public String email() {
        return email;
    }

    public URI userpic() {
        return userpic;
    }
}
