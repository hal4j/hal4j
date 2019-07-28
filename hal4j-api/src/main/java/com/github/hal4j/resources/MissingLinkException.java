package com.github.hal4j.resources;

import java.net.URI;
import java.util.function.Supplier;

public class MissingLinkException extends RuntimeException {

    public MissingLinkException(String rel) {
        super("Link does not exist or is not accessible: " + rel);
    }

    public static Supplier<MissingLinkException> missingLink(String rel) {
        return () -> new MissingLinkException(rel);
    }

    public static Supplier<MissingLinkException> missingLink(URI rel) {
        return () -> new MissingLinkException(String.valueOf(rel));
    }

}
