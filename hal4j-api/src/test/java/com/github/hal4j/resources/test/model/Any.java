package com.github.hal4j.resources.test.model;

import java.net.URI;

public final class Any {

    private Any() {
    }

    public static String name() {
        return "erikamuster";
    }

    public static String email() {
        return "erika.muster@example.com";
    }

    public static URI pictureLink() {
        return URI.create("https://www.example.com/images/erikamuster.jpg");
    }

}
