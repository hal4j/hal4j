package com.github.hal4j.jackson;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = GenericResourceSerializer.class)
@JsonDeserialize(using = GenericResourceDeserializer.class)
public abstract class GenericResourceMixin {

}
