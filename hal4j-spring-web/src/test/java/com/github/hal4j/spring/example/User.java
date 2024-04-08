package com.github.hal4j.spring.example;

import java.util.UUID;

public record User(UUID uuid, int version, String name) {
}

