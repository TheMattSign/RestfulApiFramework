package com.themattsign.apiframework.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * Created by MatthewMiddleton on 1/10/2017.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AcceptedValues {
    String[] value() default {};
    Class<?> enumClass() default Object.class;
}
