// Copyright 2020-2022 NXGN Management, LLC. All Rights Reserved.

package com.nextgen.pxp.spring.common.util.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = AtLeastOneNotBlankValidator.class)
@Documented
public @interface AtLeastOneNotBlank {

	String message() default "At least one of the fields must not be blank";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	String[] fieldNames();
}