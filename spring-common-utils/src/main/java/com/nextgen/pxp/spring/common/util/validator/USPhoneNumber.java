// Copyright 2019-2022 NXGN Management, LLC. All Rights Reserved.

package com.nextgen.pxp.spring.common.util.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Repeatable(USPhoneNumber.List.class)
@Documented
@Constraint(validatedBy = {USPhoneNumberValidator.class})
public @interface USPhoneNumber {
    String message() default "Not a valid United States phone number";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    boolean allowNull() default true;

    boolean allowZero() default false;

    @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
    @Retention(RUNTIME)
    @Documented
    @interface List {
        USPhoneNumber[] value();
    }
}