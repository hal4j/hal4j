package com.github.hal4j.spring.cloud.gw;

import com.github.hal4j.jackson.JacksonHALMapper;
import com.github.hal4j.resources.*;
import com.netflix.util.Pair;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.function.Function;

import static com.github.hal4j.resources.cloud.DiscoveryClientLinkResolver.SERVICE_NS;
import static com.github.hal4j.uritemplate.ParamHolder.prefixed;
import static com.github.hal4j.uritemplate.URIBuilder.uri;
import static com.github.hal4j.uritemplate.URIFactory.templateUri;
import static com.netflix.zuul.context.RequestContext.getCurrentContext;
import static java.util.stream.Collectors.toMap;

public class ResourceResponseFilter extends ZuulFilter {

    private String serviceId;
    private final ResourceFactory factory;
    private final JacksonHALMapper json;
    private final DiscoveryClient client;

    public ResourceResponseFilter(String serviceId,
                                  ResourceFactory factory,
                                  JacksonHALMapper json,
                                  DiscoveryClient client) {
        this.serviceId = serviceId;
        this.factory = factory;
        this.json = json;
        this.client = client;
    }

    private static class ResponseMapper implements MergeStrategy {

        private final ResourceFactory factory;
        private final Function<String, String> mapping;

        ResponseMapper(ResourceFactory factory, Function<String, String> mapping) {
            this.factory = factory;
            this.mapping = mapping;
        }

        @Override
        public HALLink map(HALLink link) {
            return HALLinkBuilder.basedOn(link).href(mapping.apply(link.href)).build();
        }

        @Override
        public Object map(Object object, BindingContext context) {
            return map(context.bind(object, GenericResource.class));
        }

        GenericResource map(GenericResource resource) {
            return factory.bindGeneric().from(resource, this).asResource();
        }
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
        String contentType = context.getOriginResponseHeaders().stream()
                .filter(pair -> "Content-Type".equals(pair.first()))
                .map(Pair::second)
                .findAny().orElse(null);
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

            ResponseMapper responseMapper = createMapper(context);
            String mappedString = json.serialize(responseMapper.map(response));

            context.setResponseBody(mappedString);
        } catch (IOException e) {
            throw new ZuulException(e, 500, "Could not process HAL response");
        }
        return null;
    }

    private ResponseMapper createMapper(RequestContext context) {
        HttpServletRequest request = context.getRequest();
        String scheme = request.getScheme();
        String host = request.getServerName();
        int port = request.getServerPort();

        Map<String, String> services = client.getServices().stream()
                .filter(serviceId::equals)
                .collect(toMap(
                        instanceId -> instanceId,
                        instanceId -> uri(scheme, host, port).relative(instanceId).toString()));

        Function<String, String> mapping = href -> templateUri(href).expand(prefixed(SERVICE_NS, services)).toString();

        return new ResponseMapper(factory, mapping);
    }
}
