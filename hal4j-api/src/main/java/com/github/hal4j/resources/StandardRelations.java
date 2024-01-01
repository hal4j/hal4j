package com.github.hal4j.resources;

import java.net.URI;

import static com.github.hal4j.resources.HALLink.REL_SELF;
import static com.github.hal4j.resources.HALLinkBuilder.alt;
import static com.github.hal4j.resources.ResourceRelation.link;

public class StandardRelations {

    public static final URI HTTP_METHOD_PATCH = URI.create("http-method:patch");
    public static final URI HTTP_METHOD_PUT = URI.create("http-method:put");
    public static final URI HTTP_METHOD_POST = URI.create("http-method:post");
    public static final URI HTTP_METHOD_DELETE = URI.create("http-method:post");

    public static final String SAME_PATH = ".";
    /**
     * Standard link for adding new object to a resource collection via HTTP POST request
     */
    public static final ResourceRelation CREATE = link(REL_SELF, alt("create").href(SAME_PATH).profile(HTTP_METHOD_POST));


    /**
     * Standard link for replacing current object with a new one via HTTP PUT request
     */
    public static final ResourceRelation REPLACE = link(REL_SELF, alt("replace").href(SAME_PATH).profile(HTTP_METHOD_PUT));

    /**
     * Standard link for updating current object via HTTP PATCH request
     */
    public static final ResourceRelation UPDATE = link(REL_SELF, alt("update").href(SAME_PATH).profile(HTTP_METHOD_PATCH));

    /**
     * Standard link for updating current object via HTTP PATCH request
     */
    public static final ResourceRelation DELETE = link(REL_SELF, alt("delete").href(SAME_PATH).profile(HTTP_METHOD_DELETE));

}
