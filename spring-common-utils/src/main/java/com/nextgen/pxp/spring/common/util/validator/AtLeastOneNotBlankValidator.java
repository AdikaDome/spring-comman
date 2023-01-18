// Copyright 2020-2022 NXGN Management, LLC. All Rights Reserved.

package com.nextgen.pxp.spring.common.util.validator;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.InvocationTargetException;

/**
 * A validator for ensuring at least one field from a given set is not blank.
 * To use it, annotate a request object with something similar to:
 * 
 * @AtLeastOneNotBlank(fieldNames={"memberId", "authId"}, message="com.nextgen.pxp.missing.memberIdOrAuthId")
 */
public class AtLeastOneNotBlankValidator implements ConstraintValidator<AtLeastOneNotBlank, Object> {

	private String[] fieldNames;

	@Override
	public void initialize(AtLeastOneNotBlank constraintAnnotation) {
		this.fieldNames = constraintAnnotation.fieldNames();
	}

	@Override
	public boolean isValid(Object object, ConstraintValidatorContext constraintContext) {
		try {
			for (String fieldName : fieldNames) {
				Object property = PropertyUtils.getProperty(object, fieldName);
				if (property != null && StringUtils.isNotBlank(String.valueOf(property))) {
					return true;
				}
			}
			return false;
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			throw new RuntimeException("An error during validation", e);
		}
	}
}