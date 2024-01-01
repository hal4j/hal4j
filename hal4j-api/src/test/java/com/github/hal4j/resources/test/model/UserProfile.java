package com.github.hal4j.resources.test.model;

import org.junit.jupiter.api.Test;

import java.net.URI;

public class UserProfile {

    private final String name;

    private final String email;

    private final URI userpic;

    public static final UserProfile someUserProfile() {
        return new UserProfile(Any.name(), Any.email(), Any.pictureLink());
    }

    public UserProfile(String name, String email, URI userpic) {
        this.name = name;
        this.email = email;
        this.userpic = userpic;
    }



}
