package com.github.hal4j.spring.web;

import com.github.hal4j.spring.MappingDiscoverer;
import com.github.hal4j.uritemplate.URIBuilder;
import com.github.hal4j.uritemplate.URITemplateVariable;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Collections;
import java.util.List;

import static com.github.hal4j.uritemplate.URITemplateModifier.MATRIX;
import static com.github.hal4j.uritemplate.URITemplateVariable.queryParam;
import static com.github.hal4j.uritemplate.URITemplateVariable.template;
import static com.github.hal4j.uritemplate.URIVarComponent.var;
import static java.util.Arrays.asList;

public class SpringWebRequestMappingDiscoverer implements MappingDiscoverer {

    private static final List<Class<? extends Annotation>> MAPPINGS = Collections.unmodifiableList(asList(
            RequestMapping.class,
            GetMapping.class,
            PostMapping.class,
            PutMapping.class,
            DeleteMapping.class,
            PatchMapping.class
    ));

    private static final List<String> FIELDS = Collections.unmodifiableList(asList("path", "value"));


    private URIBuilder getMappingFromAnnotation(AnnotatedElement element, URIBuilder builder) {
        if (element.isAnnotationPresent(RequestMapping.class)) {
            RequestMapping mapping = element.getAnnotation(RequestMapping.class);
            if (mapping.path().length > 0) {
                builder.path().append(mapping.path()[0]);
            } else if (mapping.value().length > 0) {
                builder.path().append(mapping.value()[0]);
            }
        }
        return builder;
    }

    @Override
    public URIBuilder applyMapping(Method method, URIBuilder builder) {
        getMappingFromAnnotation(method.getDeclaringClass(), builder);

        String target = "";

        outer: for (Class<? extends Annotation> type : MAPPINGS) {
            if (method.isAnnotationPresent(type)) {
                Annotation annotation = method.getAnnotation(type);
                for (String field : FIELDS) {
                    String[] values = get(annotation, field);
                    if (values.length > 0) {
                        target = values[0];
                        break outer;
                    }
                }
            }
        }

        StringBuilder pathBuilder = new StringBuilder(target);
        for (Parameter parameter : method.getParameters()) {
            if (parameter.isAnnotationPresent(RequestParam.class)) {
                RequestParam annotation = parameter.getAnnotation(RequestParam.class);
                String name = annotation.name().length() > 0
                        ? annotation.name()
                        : (annotation.value().length() > 0 ? annotation.value() : parameter.getName());
                builder.query().append(queryParam(name));
            } else if (parameter.isAnnotationPresent(MatrixVariable.class)) {
                MatrixVariable var = parameter.getAnnotation(MatrixVariable.class);
                URITemplateVariable template = template(MATRIX, var(var.name()));
                if (var.pathVar().length() > 0) {
                    String str = "{" + var.pathVar() + "}";
                    int idx = pathBuilder.indexOf(str);
                    if (idx > 0) {
                        idx += str.length();
                        pathBuilder.insert(idx, template);
                    }
                } else {
                    pathBuilder.append(template);
                }
            }
        }

        builder.path().append(pathBuilder.toString());
        return builder;
    }

    private String[] get(Annotation annotation, String field) {
        try {
            Method method = annotation.getClass().getMethod(field);
            Object result = method.invoke(annotation);
            return (String[]) result;
        } catch (Exception e) {
            return new String[0];
        }
    }

    @Override
    public URIBuilder applyMapping(Class<?> clazz, URIBuilder builder) {
        return getMappingFromAnnotation(clazz, builder);
    }
}
