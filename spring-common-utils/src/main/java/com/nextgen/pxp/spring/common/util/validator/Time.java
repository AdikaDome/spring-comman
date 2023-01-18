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
@Repeatable(Time.List.class)
@Documented
@Constraint(validatedBy = {TimeValidator.class})
public @interface Time {
    String message() default "Milliseconds time cannot be null, 0 (Epoch time), or in the past";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * This is for Long (wrapped) values
     * @return true if null is allowed
     */
    boolean allowNull() default false;

    /**
     * This is for long (primitive) values
     * @return true if EpochTime is allowed
     */
    boolean allowEpochTime() default false;

    boolean allowPastTime() default false;

    @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
    @Retention(RUNTIME)
    @Documented
    @interface List {
        Time[] value();
    }
}