package com.github.hal4j.spring.example;

import com.github.hal4j.resources.Resource;
import com.github.hal4j.resources.ResourceFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

import static com.github.hal4j.spring.example.UserController.PATH_USERS;
import static com.github.hal4j.spring.example.UserController.VAR_UUID;
import static com.github.hal4j.spring.web.RequestBasedLinkBuilder.link;
import static com.github.hal4j.uritemplate.URITemplateVariable.queryStart;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping(PATH_USERS + "/{" + VAR_UUID + "}")
public class UserController {
    public static final String PATH_USERS = API.PATH + "/users";
    public static final String VAR_UUID = "uuid";
    public static final String VAR_VERSION = "ver";

    public static final String NS = "user";

    private final ResourceFactory hal;

    private final UserRepository users;

    public UserController(ResourceFactory hal, UserRepository users) {
        this.hal = hal;
        this.users = users;
    }

    Resource<User> specificVersion(User user) {
        return hal.bind(user)
                .linkSelf().to(link().to(UserController.class).path().append(queryStart(VAR_VERSION)).asTemplate().expand(user.uuid(), user.version()))
                .asResource();
    }

    @GetMapping
    public Resource<User> latest(@PathVariable(VAR_UUID) UUID uuid) {
        var item = users.findById(uuid).orElseThrow(() -> new ResponseStatusException(NOT_FOUND));
        return hal.bind(item)
                .linkSelf().to(link().to(UserController.class).asTemplate().expand(item.uuid()))
                .link(NS  + ":version").to(link().to(UserController.class).path().append(queryStart(VAR_VERSION)).asTemplate().expandPartial(item.uuid()))
                .asResource();
    }

    @GetMapping(params = { VAR_VERSION })
    public Resource<User> version(@PathVariable(VAR_UUID) UUID uuid,
                                  @RequestParam(name = VAR_VERSION) int version) {
        var existing = users.findById(uuid).orElseThrow(() -> new ResponseStatusException(NOT_FOUND));
        var model = new User(existing.uuid(), version, existing.name());
        return hal.bind(model)
                .linkSelf().to(link().to(UserController.class).path().append(queryStart(VAR_VERSION)).asTemplate().expand(uuid, version))
                .asResource();
    }


}
