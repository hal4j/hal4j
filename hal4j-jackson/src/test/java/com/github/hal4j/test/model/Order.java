package com.github.hal4j.test.model;

import java.util.UUID;

public class Order {

    public final UUID uuid;

    public final OrderStatus status;

    public Order(UUID uuid, OrderStatus status) {
        this.uuid = uuid;
        this.status = status;
    }

}
