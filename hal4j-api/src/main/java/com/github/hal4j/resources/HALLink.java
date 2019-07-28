package com.github.hal4j.resources;

import com.github.hal4j.uritemplate.URIFactory;
import com.github.hal4j.uritemplate.URITemplate;

import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

public class HALLink implements Serializable {

    public static final String REL_SELF = "self";
    public static final String REL_NEXT = "next";
    public static final String REL_PREV = "prev";
    public static final String REL_ITEMS = "items";
    public static final String REL_CURIE = "curie";

    public static final String SAME_RESOURCE = ".";

    public final String name;

    public final String href;

    public final String hreflang;

    public final boolean templated;

    public final String title;

    public final String type;

    public final URI profile;

    public final URI deprecation;

    public HALLink(String href,
                   boolean templated,
                   String title,
                   URI name,
                   String type,
                   String hreflang,
                   URI profile,
                   URI deprecation) {
        if (!templated) {
            try {
                new URI(href);
            } catch (URISyntaxException x) {
                String msg = "href value must be a valid URI, if not a template";
                throw new IllegalArgumentException(msg, x);
            }
        }
        this.href = href;
        this.templated = templated;
        this.title = title;
        this.name = name != null ? name.toString() : null;
        this.type = type;
        this.hreflang = hreflang;
        this.profile = profile;
        this.deprecation = deprecation;
    }

    public boolean isTemplated() {
        return templated;
    }

    public String name() {
        return this.name;
    }

    public String title() {
        return this.title;
    }

    public String href() {
        return this.href;
    }

    public String type() {
        return this.type;
    }

    public URI uri() {
        return URI.create(this.href);
    }

    public String language() {
        return this.hreflang;
    }

    public URI profile() {
        return this.profile;
    }

    public URI deprecation() {
        return this.deprecation;
    }

    public URITemplate template() {
        return URIFactory.templateUri(href);
    }

    public URI expand(Map<String, ?> substitutions) {
        return this.template().expand(substitutions).toURI();
    }

    public URI expand(Object... substitutions) {
        return this.template().expand(substitutions).toURI();
    }

    public static HALLink create(URI href) {
        return new HALLink(href.toString(), false, null, null, null, null,
                null, null);
    }

};
