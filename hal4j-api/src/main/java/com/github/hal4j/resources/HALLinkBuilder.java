package com.github.hal4j.resources;

import java.net.URI;
import java.net.URISyntaxException;

import static java.util.Optional.ofNullable;

public class HALLinkBuilder implements Builder<HALLink> {

    private String href;
    private boolean templated;
    private String title;
    private URI name;
    private String type;
    private String hreflang;
    private URI profile;
    private URI deprecation;

    public static HALLinkBuilder uri(Object link) {
        return new HALLinkBuilder().href(link);
    }

    public static HALLinkBuilder alt(String name) {
        return new HALLinkBuilder().name(name);
    }

    public static HALLinkBuilder basedOn(HALLink link) {
        HALLinkBuilder builder = new HALLinkBuilder();
        builder.href = link.href;
        builder.templated = link.templated;
        builder.title = link.title;
        builder.name = ofNullable(link.name).map(URI::create).orElse(null);
        builder.type = link.type;
        builder.hreflang = link.hreflang;
        builder.profile = link.profile;
        builder.deprecation = link.deprecation;
        return builder;
    }

    private HALLinkBuilder() {
    }

    public HALLinkBuilder href(Object uri) {
        href = uri.toString();
        templated = href != null && href.indexOf('{') >= 0;
        return this;
    }

    public HALLinkBuilder lang(String languageCode) {
        this.hreflang = languageCode;
        return this;
    }

    public HALLinkBuilder title(String linkTitle) {
        this.title = linkTitle;
        return this;
    }

    public HALLinkBuilder name(URI altName) {
        this.name = altName;
        return this;
    }

    public HALLinkBuilder name(String altName) {
        if (altName != null) {
            try {
                this.name = new URI(altName);
            } catch (URISyntaxException x) {
                String msg = "name value must be a valid URI, e.g. \"rel:name\": " + name;
                throw new IllegalArgumentException(msg, x);
            }
        }
        return this;
    }

    public HALLinkBuilder type(String linkType) {
        this.type = linkType;
        return this;
    }

    public HALLinkBuilder profile(URI profileUri) {
        this.profile = profileUri;
        return this;
    }

    public HALLinkBuilder deprecate(URI deprecationUri) {
        this.deprecation = deprecationUri;
        return this;
    }

    @Override
    public HALLink build() {
        return new HALLink(href, templated, title, name, type, hreflang, profile, deprecation);
    }

}
