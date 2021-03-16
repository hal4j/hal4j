package com.github.hal4j.spring.web;

import com.github.hal4j.spring.MappingDiscoverer;
import com.github.hal4j.uritemplate.URIBuilder;
import com.github.hal4j.uritemplate.URITemplateVariable;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static com.github.hal4j.uritemplate.URITemplateModifier.MATRIX;
import static com.github.hal4j.uritemplate.URITemplateVariable.queryParam;
import static com.github.hal4j.uritemplate.URITemplateVariable.template;
import static com.github.hal4j.uritemplate.URIVarComponent.var;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

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


    private List<Object> getMappingFromAnnotation(AnnotatedElement element, List<Object> segments) {
        String path = null;
        outer: for (Class<? extends Annotation> type : MAPPINGS) {
            if (element.isAnnotationPresent(type)) {
                Annotation annotation = element.getAnnotation(type);
                for (String field : FIELDS) {
                    String[] values = get(annotation, field);
                    if (values.length > 0) {
                        path = values[0];
                        break outer;
                    }
                }
            }
        }
        if (path != null) {
            segments.addAll(Stream.of(path.split("/"))
                    .filter(s -> !s.isEmpty())
                    .map(SpringWebRequestMappingDiscoverer::replaceWithTemplate)
                    .flatMap(e -> Stream.of("/", e))
                    .collect(toList()));
        }
        return segments;
    }

    @Override
    public URIBuilder applyMapping(Method method, URIBuilder builder) {
        // start with path definition on class level
        List<Object> elements = getMappingFromAnnotation(method.getDeclaringClass(), new ArrayList<>());
        // append path definition on the method level
        elements = getMappingFromAnnotation(method, elements);

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
                String pathVar = var.pathVar();
                if (pathVar.length() > 0) {
                    for (int i = 0; i < elements.size(); i++) {
                        Object element = elements.get(i);
                        if (element instanceof URITemplateVariable) {
                            if (((URITemplateVariable) element).components().stream().anyMatch(c -> c.name().equals(pathVar))) {
                                elements.add(i + 1, template);
                                break;
                            }
                        }
                    }
                } else {
                    elements.add(template);
                }
            }
        }
        builder.path().append(elements.toArray());
        return builder;
    }

    @Override
    public URIBuilder applyMapping(Class<?> clazz, URIBuilder builder) {
        List<Object> elements = getMappingFromAnnotation(clazz, new ArrayList<>());
        builder.path().append(elements.toArray());
        return builder;
    }

    private static Object replaceWithTemplate(String raw) {
        if (raw.length() < 1) {
            return raw;
        }
        if ((raw.charAt(0) == '{') && (raw.charAt(raw.length() - 1) == '}')) {
            return URITemplateVariable.parse(raw);
        } else {
            return raw;
        }
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
}
