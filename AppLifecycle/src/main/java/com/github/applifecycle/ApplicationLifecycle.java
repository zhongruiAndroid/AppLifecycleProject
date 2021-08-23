package com.github.applifecycle;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import androidx.annotation.Keep;

@Keep
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface ApplicationLifecycle {
}
