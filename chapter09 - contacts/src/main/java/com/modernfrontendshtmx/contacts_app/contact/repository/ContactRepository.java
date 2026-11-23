package com.modernfrontendshtmx.contacts_app.contact.repository;

import com.modernfrontendshtmx.contacts_app.contact.Contact;
import com.modernfrontendshtmx.contacts_app.contact.ContactId;
import com.modernfrontendshtmx.contacts_app.infrastructure.repository.Page;

import java.util.List;
import java.util.Optional;

public interface ContactRepository {
    ContactId nextId();

    List<Contact> findAll();

    void save(Contact contact);

    Page<Contact> findAllWithNameContaining(String query,
                                            int page,
                                            int size);

    // tag::findAllOrderedByName[]
    Page<Contact> findAllOrderedByName(int page,
                                       int size);
    // end::findAllOrderedByName[]

    Optional<Contact> findById(ContactId contactId);

    void deleteById(ContactId contactId);

    boolean existsByEmail(String email);
}
