package com.github.hal4j.resources;

import com.github.hal4j.resources.curie.CurieResolver;
import com.github.hal4j.uritemplate.URIBuilder;
import com.github.hal4j.uritemplate.URITemplate;

import java.net.URI;
import java.util.*;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.github.hal4j.resources.HALLink.REL_CURIE;
import static com.github.hal4j.resources.HALLink.REL_SELF;
import static java.util.Arrays.asList;

public abstract class ResourceBuilderSupport<R extends ResourceSupport, B extends ResourceBuilderSupport<R, B>> {

    private final CurieResolver resolver;

    protected Map<String, List<HALLink>> _links;

    protected Map<String, List<Object>> _embedded;

    protected Set<String> resolvedNamespaces;

    private BindingContext context;

    protected ResourceBuilderSupport(CurieResolver resolver) {
        this.resolver = resolver;
    }

    protected abstract B _this();

    public final R asResource() {
        return build();
    }

    public B in(BindingContext ctx) {
        this.context = ctx;
        return _this();
    }

    public BindingContext context() {
        return context;
    }

    protected Map<String, List<HALLink>> links() {
        if (_links == null) {
            _links = new HashMap<>();
        }
        return _links;
    }

    protected Map<String, List<Object>> embedded() {
        if (_embedded == null) {
            _embedded = new HashMap<>();
        }
        return _embedded;
    }

    public Linker linkSelf() {
        return new Linker(URI.create(REL_SELF));
    }

    public B to(String uri) {
        return linkSelf().to(uri);
    }

    public B to(URIBuilder builder) {
        return linkSelf().to(builder);
    }

    public Linker link(URI rel) {
        return new Linker(rel);
    }

    public Linker link(String rel) {
        return new Linker(URI.create(rel));
    }

    private List<HALLink> mutableLinks(URI rel) {
        resolve(rel);
        return this.links().computeIfAbsent(rel.toString(), any -> new ArrayList<>());
    }

    public B embed(String rel, Object... objects) {
        return embed(URI.create(rel), objects);
    }

    public B embed(URI rel, Object... objects) {
        resolve(rel);
        List<Object> existing = this.embedded().computeIfAbsent(rel.toString(), any -> new ArrayList<>());
        existing.addAll(asList(objects));
        return _this();
    }

    private void resolve(URI uri) {
        if (uri == null) {
            throw new NullPointerException("uri");
        }
        String rel = uri.toString();
        int idx = rel.indexOf(':');
        if (idx < 0) return;
        if (idx == 1) {
            throw new IllegalArgumentException("Invalid relation: \"" + rel + "\"");
        }
        String ns = rel.substring(0, idx);
        if (resolvedNamespaces != null) {
            if (resolvedNamespaces.contains(ns)) return;
        } else {
            resolvedNamespaces = new HashSet<>();
        }
        resolvedNamespaces.add(ns);
        URITemplate resolved = resolver.resolve(ns);
        if (resolved != null) {
            HALLink link = HALLinkBuilder.uri(resolved.toString()).name(ns).build();
            this.link(REL_CURIE).to(link);
        }
    }

    public abstract R build();

    public B merge(ResourceSupport resource, MergeStrategy strategy) {
        resource.links().all().forEach((rel, list) -> {
            List<HALLink> links = list.stream().map(strategy::map).collect(Collectors.toList());
            this.links().put(rel, links);
        });
        resource.embedded().all().forEach((rel, list) -> {
            List<Object> objects = list.stream()
                    .map(object -> strategy.map(object, resource.context()))
                    .collect(Collectors.toList());
            this.embedded().put(rel, objects);
        });
        return _this();
    }

    public class Linker {

        private final URI rel;

        private boolean accepted = true;

        public Linker(URI rel) {
            this.rel = rel;
        }

        public Linker when(boolean condition) {
            this.accepted = condition;
            return this;
        }

        public Linker when(Predicate<URI> voter) {
            this.accepted = voter.test(rel);
            return this;
        }

        public Linker when(Supplier<Boolean> condition) {
            this.accepted = condition.get();
            return this;
        }

        public B to(HALLink link) {
            if (accepted) {
                List<HALLink> relLinks = mutableLinks(rel);
                relLinks.add(link);
            }
            return _this();
        }

        public B to(HALLinkBuilder linkBuilder) {
            if (accepted) {
                List<HALLink> relLinks = mutableLinks(rel);
                relLinks.add(linkBuilder.build());
            }
            return _this();
        }

        public B to(String uri) {
            return to(HALLinkBuilder.uri(uri));
        }

        public B to(URI href) {
            if (href == null) {
                throw new NullPointerException("href");
            }
            return to(href.toString());
        }

        public B to(URITemplate href) {
            if (href == null) {
                throw new NullPointerException("href");
            }
            return to(href.toString());
        }

        public B to(URIBuilder href) {
            if (href == null) {
                throw new NullPointerException("href");
            }
            return to(href.toString());
        }

        public B toAll(HALLinkBuilder... builders) {
            if (accepted) {
                List<HALLink> relLinks = mutableLinks(rel);
                Stream.of(builders).map(HALLinkBuilder::build).forEach(relLinks::add);
            }
            return _this();
        }

        public B toAll(Collection<HALLink> links) {
            if (accepted) {
                List<HALLink> relLinks = mutableLinks(rel);
                relLinks.addAll(links);
            }
            return _this();
        }

    }

}
