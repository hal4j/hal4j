package com.github.hal4j.resources.curie;

import com.github.hal4j.uritemplate.URITemplate;

public interface CurieResolver {

    URITemplate resolve(String namespace);

}
