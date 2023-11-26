package com.modernfrontendshtmx.contactsapp.contact.service;

import com.modernfrontendshtmx.contactsapp.contact.Contact;
import com.modernfrontendshtmx.contactsapp.contact.ContactId;
import com.modernfrontendshtmx.contactsapp.contact.ContactNotFoundException;
import com.modernfrontendshtmx.contactsapp.contact.repository.ContactRepository;
import com.modernfrontendshtmx.contactsapp.infrastructure.repository.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactService {
    private final ContactRepository repository;

    public ContactService(ContactRepository repository) {
        this.repository = repository;
    }

    public List<Contact> getAll() {
        return repository.findAll();
    }

    public Contact storeNewContact(String givenName,
                                   String familyName,
                                   String phone,
                                   String email) { // <.>
        Contact contact = new Contact(repository.nextId(),
                givenName,
                familyName,
                phone,
                email);
        repository.save(contact);
        return contact;
    }

    // tag::updateContact[]
    public void updateContact(ContactId contactId,
                              String givenName,
                              String familyName,
                              String phone,
                              String email) {
        Contact contact = getContact(contactId);
        contact.setGivenName(givenName);
        contact.setFamilyName(familyName);
        contact.setPhone(phone);
        contact.setEmail(email);

        repository.save(contact);
    }

    public void deleteContact(ContactId contactId) {
        repository.deleteById(contactId);
    }
    // end::updateContact[]

    // tag::searchContacts[]
    public Page<Contact> searchContacts(String query,
                                        int page) {
        return repository.findAllWithNameContaining(query,
                page,
                10);
    }
    // end::searchContacts[]

    // tag::getAll[]
    public Page<Contact> getAll(int page) {
        return repository.findAllOrderedByName(page, 10);
    }
    // end::getAll[]

    // tag::getContact[]
    public Contact getContact(ContactId contactId) {
        return repository.findById(contactId)
                .orElseThrow(() -> new ContactNotFoundException(contactId));
    }
    // end::getContact[]

    // tag::contactWithEmailExists[]
    public boolean contactWithEmailExists(String email) {
        return repository.existsByEmail(email);
    }
    // end::contactWithEmailExists[]
}
