package com.github.hal4j.spring.example;

import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/resources")
public class ResourcesController {

    @GetMapping("/recent")
    public List<String> findRecent() {
        return Collections.emptyList();
    }

    @GetMapping(params = { "owner" })
    public List<String> findByPatient(@RequestParam("owner") UUID uuid) {
        return Collections.emptyList();
    }

    @GetMapping("/{id}/meta")
    public List<String> fetchMetadata(@PathVariable("id") UUID uuid,
                                      @MatrixVariable(name = "tag", pathVar = "id") String tag) {
        return Collections.emptyList();
    }

}
