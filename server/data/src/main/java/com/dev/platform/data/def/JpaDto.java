package com.dev.platform.data.def;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Documented
@Component
@Target(value = {ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface JpaDto {
}
