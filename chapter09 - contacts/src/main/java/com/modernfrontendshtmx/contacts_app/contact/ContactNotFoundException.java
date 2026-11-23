package com.modernfrontendshtmx.contacts_app.contact;

public class ContactNotFoundException extends RuntimeException {
    public ContactNotFoundException(ContactId contactId) {
        super("Could not find contact with id " + contactId);
    }
}
