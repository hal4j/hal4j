package com.github.hal4j.spring.cloud.gw;

import com.github.hal4j.jackson.JacksonHALMapper;
import com.github.hal4j.resources.*;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Function;

import static com.netflix.zuul.context.RequestContext.getCurrentContext;

public class ResourceResponseFilter extends ZuulFilter implements MergeStrategy {

    private final ResourceFactory factory;
    private final JacksonHALMapper json;
    private final Function<String, String> mapper;

    public ResourceResponseFilter(ResourceFactory factory,
                                  JacksonHALMapper json,
                                  Function<String, String> mapper) {
        this.factory = factory;
        this.json = json;
        this.mapper = mapper;
    }

    public GenericResource map(GenericResource resource) {
        return factory.bindGeneric().from(resource, this).asResource();
    }

    @Override
    public HALLink map(HALLink link) {
        return HALLinkBuilder.basedOn(link).href(mapper.apply(link.href)).build();
    }

    @Override
    public Object map(Object object, BindingContext context) {
        return map(context.bind(object, GenericResource.class));
    }

    @Override
    public String filterType() {
        return "post";
    }

    @Override
    public int filterOrder() {
        return 999;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext context = getCurrentContext();
        String contentType = context.getResponse().getHeader("Content-Type");
        return context.getResponseStatusCode() < 300
                && contentType != null
                && contentType.startsWith("application/json");
    }

    @Override
    public Object run() throws ZuulException {
        try {
            RequestContext context = getCurrentContext();
            InputStream stream = context.getResponseDataStream();
            GenericResource response = json.mapper().readValue(stream, GenericResource.class);
            String mappedString = json.serialize(map(response));
            context.setResponseBody(mappedString);
        } catch (IOException e) {
            throw new ZuulException(e, 500, "Could not process HAL response");
        }
        return null;
    }
}
