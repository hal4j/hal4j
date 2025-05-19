package com.github.hal4j.jackson;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.hal4j.resources.BindingContext;
import com.github.hal4j.resources.HALLink;
import com.github.hal4j.resources.ResourceBuilderSupport;
import com.github.hal4j.resources.ResourceSupport;
import com.github.hal4j.resources.curie.CurieResolver;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * A model or resource links and embedded objects embeddable in records.
 * Offers an alternative to a Resource&lt;T&gt; wrapper for better composition of resource objects.
 * For example, in the following code account resource is nested in the profile resource.
 * <pre><code>
 *     ResourceFactory hal = ...
 *     URI self = ...
 *     EmbeddedResources resources = hal.bind(resources()).as(self)
 *           .link(...).embed(...)
 *           .asResource();
 *     Account account = new Account(accountId, username, resources);
 *     UserProfile record = new UserProfile(userId, email, account);
 * </code></pre>
 * This solution is useful in the MVVM pattern, where a domain-specific view model (accounts and user profiles in the
 * example above) aggregates both data and hyperlinks.
 */
//@JsonDeserialize(using = EmbeddedResourcesDeserializer.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmbeddedResources extends ResourceSupport {

    public static class Builder extends ResourceBuilderSupport<EmbeddedResources, Builder> {
        protected Builder(CurieResolver resolver) {
            super(resolver);
        }
        @Override
        protected Builder _this() {
            return this;
        }
        @Override
        public EmbeddedResources build() {
            return new EmbeddedResources(_links, _embedded, context());
        }

    }

    /**
     * This function returns a builder of EmbeddedResources using the given CurieResolver. Usage:
     * <pre><code>
     *     ResourceFactory factory = ...
     *     EmbeddedResources resources = factory.bind(resources())
     *        ...
     *        .asResource();
     *     SomeViewModel vm = new SomeViewModel(..., resources);
     * </code></pre>
     * @see com.github.hal4j.resources.ResourceFactory#bind(Function)
     * @return a factory function for a builder of EmbeddedResources instance
     */
    public static Function<CurieResolver, Builder> resources() {
        return Builder::new;
    }

    public EmbeddedResources(@JsonProperty("_links") Map<String, List<HALLink>> _links,
                             @JsonProperty("_embedded") Map<String, List<Object>> _embedded,
                             @JacksonInject("context") BindingContext context) {
        super(_links, _embedded, context);
    }

    @JsonGetter("_links")
    public Map<String, List<HALLink>> linksAsMap() {
        return super.links().asIs();
    }

    @JsonGetter("_embedded")
    public Map<String, List<Object>> embeddedAsMap() {
        return super.embedded().asIs();
    }

}

