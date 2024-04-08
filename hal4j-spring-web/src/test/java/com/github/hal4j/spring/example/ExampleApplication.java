package com.github.hal4j.spring.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.hal4j.jackson.HALObjectMapperFactory;
import com.github.hal4j.jackson.JacksonResourceFactory;
import com.github.hal4j.resources.ResourceFactory;
import com.github.hal4j.resources.curie.TemplateCurieResolver;
import com.github.hal4j.uritemplate.URIBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import static com.github.hal4j.resources.curie.TemplateCurieResolver.CURIE_NS;
import static com.github.hal4j.uritemplate.URIBuilder.basedOn;
import static com.github.hal4j.uritemplate.URITemplateOperator.PATH;
import static com.github.hal4j.uritemplate.URITemplateVariable.template;
import static com.github.hal4j.uritemplate.URIVarComponent.var;

@SpringBootApplication
public class ExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExampleApplication.class);
    }

    @Bean
    public ResourceFactory resourceFactory(ObjectMapper mapper) {
        var resolver = new TemplateCurieResolver(basedOn("http://localhost:8080/docs/v1").append(template(PATH, var(CURIE_NS), var("rel"))).asTemplate());
        return new JacksonResourceFactory(resolver, mapper);
    }

    @Bean(name = "objectMapper")
    @Primary
    public ObjectMapper mapper() {
        return HALObjectMapperFactory.createStrictMapper();
    }

}
