package com.finekuo.normalcore.annotation;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.finekuo.normalcore.util.json.MaskedStringJacksonSerializer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@JsonSerialize(using = MaskedStringJacksonSerializer.class)
public @interface MaskedString {
}
