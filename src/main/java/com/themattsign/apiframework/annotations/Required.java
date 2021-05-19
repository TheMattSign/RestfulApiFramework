package com.themattsign.apiframework.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * Created by MatthewMiddleton on 7/11/2016.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Required {

    enum RequiredActionEnum {CREATE, UPDATE, BOTH}

    RequiredActionEnum value() default RequiredActionEnum.BOTH;
}
