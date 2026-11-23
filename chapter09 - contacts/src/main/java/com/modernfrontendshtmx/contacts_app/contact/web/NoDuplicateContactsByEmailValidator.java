package com.modernfrontendshtmx.contacts_app.contact.web;

import com.modernfrontendshtmx.contacts_app.contact.service.ContactService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NoDuplicateContactsByEmailValidator implements ConstraintValidator<NoDuplicateContactsByEmail, CreateContactFormData> { // <.>
    private final ContactService contactService;

    public NoDuplicateContactsByEmailValidator(ContactService contactService) {
        this.contactService = contactService;
    }

    @Override
    public boolean isValid(CreateContactFormData formData,
                           ConstraintValidatorContext context) {
        if (contactService.contactWithEmailExists(formData.getEmail())) { // <.>
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("There is already a contact with this email address")
                   .addPropertyNode("email") // <.>
                   .addConstraintViolation();

            return false;
        }

        return true;
    }
}
