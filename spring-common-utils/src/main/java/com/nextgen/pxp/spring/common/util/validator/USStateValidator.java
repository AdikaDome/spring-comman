// Copyright 2022 NXGN Management, LLC. All Rights Reserved.

package com.nextgen.pxp.spring.common.util.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class USStateValidator implements ConstraintValidator<USState, String> {

    private static final String VALID_STATE = "Alabama|Alaska|American Samoa|Arizona|Arkansas|California|Colorado|Connecticut|Delaware|District Of Columbia|Federated States Of Micronesia|Florida|Georgia|Guam|Hawaii|Idaho|Illinois|Indiana|Iowa|Kansas|Kentucky|Louisiana|Maine|Marshall Islands|Maryland|Massachusetts|Michigan|Minnesota|Mississippi|Missouri|Montana|Nebraska|Nevada|New Hampshire|New Jersey|New Mexico|New York|North Carolina|North Dakota|Northern Mariana Islands|Ohio|Oklahoma|Oregon|Palau|Pennsylvania|Puerto Rico|Rhode Island|South Carolina|South Dakota|Tennessee|Texas|Utah|Vermont|Virgin Islands|Virginia|Washington|West Virginia|Wisconsin|Wyoming";
    private static final String VALID_STATE_ABBREVIATIONS = "AL|AK|AS|AZ|AR|CA|CO|CT|DE|DC|FL|GA|GU|HI|ID|IL|IN|IA|KS|KY|LA|ME|MD|MA|MI|MN|MS|MO|MT|NE|NV|NH|NJ|NM|NY|NC|ND|MP|OH|OK|OR|PA|PR|RI|SC|SD|TN|TX|UT|VT|VI|VA|WA|WV|WI|WY";

    private boolean allowNull;
    private boolean allowAbbreviated;

    private Pattern pattern;
    private Pattern abbreviated;

    @Override
    public void initialize(USState usState) {
        this.allowNull = usState.allowNull();
        this.allowAbbreviated = usState.allowAbbreviated();
        this.pattern = Pattern.compile(VALID_STATE);
        this.abbreviated = Pattern.compile(VALID_STATE_ABBREVIATIONS);
    }

    @Override
    public boolean isValid(String input, ConstraintValidatorContext constraintValidatorContext) {
        if (input == null) {
            return allowNull;
        }
        if (allowAbbreviated) {
            return pattern.matcher(input).matches() || abbreviated.matcher(input).matches();
        }

        return pattern.matcher(input).matches();
    }
}