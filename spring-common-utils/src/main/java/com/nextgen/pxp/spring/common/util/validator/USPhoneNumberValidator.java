// Copyright 2022 NXGN Management, LLC. All Rights Reserved.

package com.nextgen.pxp.spring.common.util.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class USPhoneNumberValidator implements ConstraintValidator<USPhoneNumber, Long> {

    private static final long TEN_DIGIT_PHONE_NUMBER = 1000000000L;
    private static final long ELEVEN_DIGIT_PHONE_NUMBER = 19999999999L;
    private boolean allowNull;
    private boolean allowZero;

    @Override
    public void initialize(USPhoneNumber usPhoneNumber) {
        this.allowNull = usPhoneNumber.allowNull();
        this.allowZero = usPhoneNumber.allowZero();
    }

    @Override
    public boolean isValid(Long input, ConstraintValidatorContext constraintValidatorContext) {
        if (input == null) {
            return allowNull;
        }

        if (input == 0) {
            return allowZero;
        }

        return input >= TEN_DIGIT_PHONE_NUMBER && input <= ELEVEN_DIGIT_PHONE_NUMBER;
    }
}