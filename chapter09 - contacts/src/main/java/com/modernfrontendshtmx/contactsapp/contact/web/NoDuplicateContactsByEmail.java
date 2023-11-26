package com.modernfrontendshtmx.contactsapp.contact.web;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NoDuplicateContactsByEmailValidator.class)
public @interface NoDuplicateContactsByEmail {
    String message() default "There is already a contact with this email address";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
