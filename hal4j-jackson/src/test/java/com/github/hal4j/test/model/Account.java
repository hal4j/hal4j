package com.github.hal4j.test.model;

import java.io.Serializable;

public class Account implements Serializable {

    public final String name;

    public final String email;

    public final Address address;

    public final Money balance;

    public Account(String name,
                   String email,
                   Address address,
                   Money balance) {
        this.name = name;
        this.email = email;
        this.address = address;
        this.balance = balance;
    }
}
