package com.github.hal4j.resources;

import java.net.URI;

public interface ResourceObject {

    URI self() throws MissingLinkException;

    Links links();

    EmbeddedObjects embedded();

}
