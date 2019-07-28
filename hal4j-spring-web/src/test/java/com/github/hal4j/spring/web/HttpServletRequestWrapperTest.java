package com.github.hal4j.spring.web;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockServletContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

public class HttpServletRequestWrapperTest {

    @Test
    public void shouldCorrectlyParseSimpleRequest() {
        MockServletContext context = new MockServletContext();
        MockHttpServletRequest request = get("https://www.example.com:443").buildRequest(context);
        HttpServletRequestWrapper wrapped = new HttpServletRequestWrapper(request);
        assertEquals("https://www.example.com", wrapped.uri().toString());
    }

    @Test
    public void shouldCorrectlyHandleForwardedRequest() {
        MockServletContext context = new MockServletContext();
        MockHttpServletRequest request = get("http://192.168.0.1")
                .header("X-Forwarded-Proto", "https")
                .header("X-Forwarded-Host", "www.example.com")
                .buildRequest(context);
        HttpServletRequestWrapper wrapped = new HttpServletRequestWrapper(request);
        assertEquals("https://www.example.com", wrapped.uri().toString());
    }

    @Test
    public void shouldCorrectlyHandleForwardedRequestToServletContext() {
        MockServletContext context = new MockServletContext("/ctx");
        MockHttpServletRequest request = get("http://192.168.0.1/ctx")
                .header("X-Forwarded-Proto", "https")
                .header("X-Forwarded-Host", "www.example.com")
                .buildRequest(context);
        HttpServletRequestWrapper wrapped = new HttpServletRequestWrapper(request);
        assertEquals("https://www.example.com/ctx", wrapped.uri().toString());
    }

}
