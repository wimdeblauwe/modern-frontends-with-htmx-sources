package com.modernfrontendshtmx.contactsapp.contact;

public class ContactNotFoundException extends RuntimeException {
    public ContactNotFoundException(ContactId contactId) {
        super("Could not find contact with id " + contactId);
    }
}
