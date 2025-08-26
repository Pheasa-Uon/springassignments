package com.pheasa.springassignments.configuration.annotations;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface MyRetryable {
    int maxRetries() default 3;
    long retryDelay() default 1000; // in milliseconds
    Class<? extends Throwable>[] retryFor() default {Exception.class};
    Class<? extends Throwable>[] noRetryFor() default {};
}
