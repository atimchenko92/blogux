package de.hska.lkit.blogux.util;

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.ElementType;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import java.lang.annotation.Documented;

@Documented
@Constraint(validatedBy = UserExistsValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface UserExistsConstraint {
    String message() default "User with this username already exists";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
