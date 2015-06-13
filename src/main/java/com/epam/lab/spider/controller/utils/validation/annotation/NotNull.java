package com.epam.lab.spider.controller.utils.validation.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by shell on 6/13/2015.
 */

@Target( ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NotNull {
    String message() default "{com.epam.lab.spider.controller.utils.validation.annotation.NotNull.message}";

}