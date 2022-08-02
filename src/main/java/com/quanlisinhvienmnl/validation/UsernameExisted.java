package com.quanlisinhvienmnl.validation;


import com.quanlisinhvienmnl.validation.impl.UsernameExistedValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = UsernameExistedValidator.class)
@Documented
public @interface UsernameExisted {
    String message() default "{message.username.existed}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
