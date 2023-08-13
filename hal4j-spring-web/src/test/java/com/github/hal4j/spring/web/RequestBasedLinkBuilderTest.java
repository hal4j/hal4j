package com.github.hal4j.spring.web;

import com.github.hal4j.spring.example.ResourceController;
import com.github.hal4j.spring.example.ResourcesController;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.ReflectionUtils;
import org.springframework.mock.web.MockHttpServletRequest;

import java.lang.reflect.Method;
import java.util.UUID;

import static com.github.hal4j.spring.example.RequestBuilders.givenRequested;
import static com.github.hal4j.spring.web.RequestBasedLinkBuilder.link;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

class RequestBasedLinkBuilderTest {

    @Test
    void shouldBuildCorrectLinkForHttpsRequestToLocalhost() {
        String originalURI = "https://localhost:8443/api/v1/resources/recent";
        HttpServletRequest request = givenRequested(get(originalURI), "/");
        assertNotNull(request);
        assertEquals(originalURI, link().to(ResourcesController.class).relative("recent").toString());
    }

    @Test
    void shouldBuildCorrectTemplateLinkForHttpsRequestToLocalhost() {
        UUID uuid = UUID.randomUUID();
        String originalURI = "https://localhost:8443/api/v1/resources/" + uuid + "/status";
        HttpServletRequest request = givenRequested(get(originalURI), "/");
        assertNotNull(request);
        assertEquals(originalURI, link().to(ResourceController.class).relative("status").asTemplate().expand(uuid).toString());
    }

    @Test
    void shouldBuildCorrectLinkForForwardedRequestProxy() {
        String originalURI = "https://api.example.com:8443/api/v1/resources/recent";
        MockHttpServletRequest request = givenRequested(get("https://192.168.1.1:8480/api/v1/resources/recent")
                .header("X-Forwarded-Host", "api.example.com:8443"), "/");
        assertNotNull(request);
        assertEquals(originalURI, link().to(ResourcesController.class).relative("recent").toString());
    }

    @Test
    void shouldBuildCorrectLinkForForwardedHttpsToHttpRequestLB() {
        String originalURI = "https://api.example.com/api/v1/resources/recent";
        MockHttpServletRequest request = givenRequested(get("http://192.168.1.1/api/v1/resources/recent")
                .header("X-Forwarded-Host", "api.example.com")
                .header("X-Forwarded-Proto", "https"), "/");
        request.setServerPort(-1); // workaround for a bug in MockHttpServletRequestBuilder (wrong default port for https)
        assertNotNull(request);
        assertEquals(originalURI, link().to(ResourcesController.class).relative("recent").toString());
    }

    @Test
    void shouldBuildCorrectLinkForForwardedHttpsToHttpRequestZuul() {
        String originalURI = "https://api.example.com/serv/api/v1/resources/recent";
        MockHttpServletRequest request = givenRequested(get("http://192.168.1.1:8480/api/v1/resources/recent")
                .header("X-Forwarded-Host", "api.example.com")
                .header("X-Forwarded-Proto", "https")
                .header("X-Forwarded-Prefix", "/serv"), "/");
        assertNotNull(request);
        assertEquals(originalURI, link().to(ResourcesController.class).relative("recent").toString());
    }

    @Test
    void shouldBuildCorrectLinkForForwardedHttpsToHttpRequestZuulAlternativePort() {
        String originalURI = "https://api.example.com:8443/serv/api/v1/resources/recent";
        MockHttpServletRequest request = givenRequested(get("http://192.168.1.1:8480/api/v1/resources/recent")
                .header("X-Forwarded-Host", "api.example.com:8443")
                .header("X-Forwarded-Proto", "https")
                .header("X-Forwarded-Prefix", "/serv"), "/");
        assertNotNull(request);
        assertEquals(originalURI, link().to(ResourcesController.class).relative("recent").toString());
    }

    @Test
    void shouldRenderMatrixVariables() {
        String originalURI = "https://api.example.com/serv/api/v1/resources/{id}{;tag}/meta";
        MockHttpServletRequest request = givenRequested(get("http://192.168.1.1:8480/api/v1/resources")
                .header("X-Forwarded-Host", "api.example.com")
                .header("X-Forwarded-Proto", "https")
                .header("X-Forwarded-Prefix", "/serv"), "/");
        assertNotNull(request);
        Method method = ReflectionUtils.findMethod(ResourcesController.class, "fetchMetadata", UUID.class, String.class).orElseThrow(IllegalStateException::new);
        assertEquals(originalURI, link().to(method).toString());
    }


}

