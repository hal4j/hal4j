package com.github.hal4j.jackson;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.type.TypeBindings;

public class ResourceHandlerInstantiator extends HandlerInstantiator {

    private final HandlerInstantiator delegate;

    public ResourceHandlerInstantiator(HandlerInstantiator delegate) {
        this.delegate = delegate != null ? delegate : new NoOpHandlerInstantiator();
    }

    @Override
    public JsonDeserializer<?> deserializerInstance(DeserializationConfig config,
                                                    Annotated annotated,
                                                    Class<?> deserClass) {
        if (ResourcesDeserializer.class.isAssignableFrom(deserClass)) {
            TypeBindings bindings = annotated.getType().getBindings();
            JavaType modelType = bindings.getTypeParameters().get(0);
            return new ResourcesDeserializer(modelType);
        }else if (ResourceDeserializer.class.isAssignableFrom(deserClass)) {
            TypeBindings bindings = annotated.getType().getBindings();
            JavaType modelType = bindings.getTypeParameters().get(0);
            return new ResourceDeserializer(modelType);
        }
        return delegate.deserializerInstance(config, annotated, deserClass);
    }

    @Override
    public KeyDeserializer keyDeserializerInstance(DeserializationConfig config,
                                                   Annotated annotated,
                                                   Class<?> keyDeserClass) {
        return delegate.keyDeserializerInstance(config, annotated, keyDeserClass);
    }

    @Override
    public JsonSerializer<?> serializerInstance(SerializationConfig config,
                                                Annotated annotated,
                                                Class<?> serClass) {
        return delegate.serializerInstance(config, annotated, serClass);
    }

    @Override
    public TypeResolverBuilder<?> typeResolverBuilderInstance(MapperConfig<?> config,
                                                              Annotated annotated,
                                                              Class<?> builderClass) {
        return delegate.typeResolverBuilderInstance(config, annotated, builderClass);
    }

    @Override
    public TypeIdResolver typeIdResolverInstance(MapperConfig<?> config,
                                                 Annotated annotated,
                                                 Class<?> resolverClass) {
        return typeIdResolverInstance(config, annotated, resolverClass);
    }

}
