package com.github.hal4j.spring.example;

import com.github.hal4j.resources.Resource;
import com.github.hal4j.resources.ResourceFactory;
import com.github.hal4j.resources.Resources;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.github.hal4j.spring.example.UserController.NS;
import static com.github.hal4j.spring.example.UserController.PATH_USERS;
import static com.github.hal4j.spring.web.RequestBasedLinkBuilder.link;
import static com.github.hal4j.uritemplate.URITemplateVariable.queryStart;

@RestController
@RequestMapping(PATH_USERS)
public class UsersController {

    private final ResourceFactory hal;
    private final UserRepository users;

    private final UserController resource;

    public UsersController(ResourceFactory hal, UserRepository users, UserController resource) {
        this.hal = hal;
        this.users = users;
        this.resource = resource;
    }

    @GetMapping
    public Resources<User> findRecent() {
        return hal.bindAll(User.class, users.findAll())
                .as(resource::specificVersion)
                .linkSelf().to(link().to(UsersController.class))
                .link(NS + ":find-all-by-version").to(link().to(UsersController.class).append(queryStart("version")).asTemplate())
                .asResource();
    }

    @GetMapping(params = { "version" })
    public Resources<User> findByVersion(@RequestParam("version") int version) {
        return hal.bindAll(User.class, users.findAllByVersion(version)).as(resource::specificVersion)
                .linkSelf().to(link().to(UsersController.class).append(queryStart("version")).asTemplate().expand(version))
                .asResource();
    }

    @GetMapping(path = "/{id}/meta")
    public Resource<User> fetchMetadata(@PathVariable(name = "id") UUID id,
                                        @MatrixVariable(pathVar = "id") String tag) {
        return hal.bind(users.findById(id).orElseThrow()).linkSelf().to(link().to(UsersController.class)).asResource();
    }



}
