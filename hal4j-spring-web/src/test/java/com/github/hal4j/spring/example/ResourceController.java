package com.github.hal4j.spring.example;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/resources/{uuid}")
public class ResourceController {

    @GetMapping("/status")
    public String status(@PathVariable("uuid") UUID uuid) {
        return "OK";

    }

}
