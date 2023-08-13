package com.github.hal4j.spring.web;

import com.github.hal4j.spring.HypermediaRequest;

import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

public class HttpServletRequestWrapper implements HypermediaRequest {

    private final HttpServletRequest request;

    private final String scheme;
    private final String host;
    private final int port;
    private final String prefix;

    public HttpServletRequestWrapper(HttpServletRequest request) {
        if (request == null) {
            throw new NullPointerException("request");
        }
        this.request = request;
        int port = -1;
        String scheme = request.getHeader("x-forwarded-proto");
        if (scheme != null) {
            this.scheme = scheme;
        } else if ("on".equals(request.getHeader("X-Forwarded-Ssl"))) {
            this.scheme = "https";
        } else {
            this.scheme = request.getScheme();
        }

        String hostHeader = "x-forwarded-host";
        String host = request.getHeader(hostHeader);
        if (host == null) {
            hostHeader = "host";
            host = request.getHeader(hostHeader);
        }
        if (host == null) {
            host = request.getServerName();
            port = request.getServerPort();
        }
        int pos = host.indexOf(':');
        if (pos > 0) {
            this.host = host.substring(0, pos);
            try {
                port = Integer.parseInt(host.substring(pos + 1));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid request header value " + hostHeader + ": " + host);
            }
        } else {
            this.host = host;
        }
        if ("https".equals(this.scheme) && port == 443) {
            port = -1;
        } else if ("http".equals(this.scheme) && port == 80) {
            port = -1;
        }
        this.port = port;
        this.prefix = Optional.ofNullable(request.getHeader("x-forwarded-prefix")).orElse("");
    }

    @Override
    public String scheme() {
        return scheme;
    }

    @Override
    public String host() {
        return host;
    }

    @Override
    public int port() {
        return port;
    }

    @Override
    public HypermediaRequest resolved() {
        return new HypermediaRequest() {
            @Override
            public String scheme() {
                return request.getScheme();
            }

            @Override
            public String host() {
                return request.getServerName();
            }

            @Override
            public int port() {
                return request.getServerPort();
            }

            @Override
            public HypermediaRequest resolved() {
                return this;
            }

            @Override
            public String pathPrefix() {
                return "";
            }
        };
    }

    @Override
    public String pathPrefix() {
        return prefix;
    }

    public URI uri() {
        URI uri = URI.create(request.getRequestURI());
        try {
            return new URI(scheme, null, host, port, uri.getPath(), uri.getQuery(), uri.getFragment());
        } catch (URISyntaxException e) {
            throw new IllegalStateException("Invalid request", e);
        }
    }
}
