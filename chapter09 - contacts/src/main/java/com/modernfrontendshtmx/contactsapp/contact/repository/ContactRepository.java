package com.modernfrontendshtmx.contactsapp.contact.repository;

import com.modernfrontendshtmx.contactsapp.contact.Contact;
import com.modernfrontendshtmx.contactsapp.contact.ContactId;
import com.modernfrontendshtmx.contactsapp.infrastructure.repository.Page;

import java.util.List;
import java.util.Optional;

public interface ContactRepository {
    ContactId nextId(); // <.>

    List<Contact> findAll();

    void save(Contact contact); // <.>

    // tag::findAllWithNameContaining[]
    Page<Contact> findAllWithNameContaining(String query,
                                            int page,
                                            int size);
    // end::findAllWithNameContaining[]

    // tag::findAllOrderedByName[]
    Page<Contact> findAllOrderedByName(int page,
                                       int size);
    // end::findAllOrderedByName[]

    // tag::findById[]
    Optional<Contact> findById(ContactId contactId);
    // end::findById[]

    // tag::deleteById[]
    void deleteById(ContactId contactId);
    // end::deleteById[]

    // tag::existsByEmail[]
    boolean existsByEmail(String email);
    // end::existsByEmail[]
}
