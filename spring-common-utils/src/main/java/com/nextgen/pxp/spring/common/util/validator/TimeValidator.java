// Copyright 2022 NXGN Management, LLC. All Rights Reserved.

package com.nextgen.pxp.spring.common.util.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.Instant;

public class TimeValidator implements ConstraintValidator<Time, Long> {

    private boolean allowNull;
    private boolean allowEpochTime;
    private boolean allowPastTime;

    @Override
    public void initialize(Time time) {
        this.allowNull = time.allowNull();
        this.allowEpochTime = time.allowEpochTime();
        this.allowPastTime = time.allowPastTime();
    }

    @Override
    public boolean isValid(Long input, ConstraintValidatorContext constraintValidatorContext) {
        if (input == null) {
           return allowNull;
        }

        if (input == 0) {
            return allowEpochTime;
        }

        if (input < 0) {
            return false;
        }

        long now = Instant.now().toEpochMilli();

        if (input < now) {
            return allowPastTime;
        }

        return input > now;
    }
}