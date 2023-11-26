package com.modernfrontendshtmx.contactsapp.contact.repository;

import com.modernfrontendshtmx.contactsapp.contact.Contact;
import com.modernfrontendshtmx.contactsapp.contact.ContactId;
import com.modernfrontendshtmx.contactsapp.infrastructure.repository.Page;
import net.datafaker.Faker;
import net.datafaker.providers.base.Name;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryContactRepository implements ContactRepository {

    private final AtomicLong sequence = new AtomicLong(); // <.>
    private final Map<ContactId, Contact> values = new HashMap<>();

    // tag::constructor[]
    public InMemoryContactRepository() {
        Faker faker = new Faker();
        for (int i = 0; i < 100; i++) {
            Name name = faker.name();
            String firstName = name.firstName();
            String lastName = name.lastName();
            save(new Contact(nextId(),
                    firstName,
                    lastName,
                    faker.phoneNumber().phoneNumber(),
                    faker.internet().emailAddress(firstName.toLowerCase(Locale.ROOT) + "." + lastName.toLowerCase(Locale.ROOT))));
        }
    }
    // end::constructor[]

    @Override
    public ContactId nextId() {
        return new ContactId(sequence.incrementAndGet()); // <.>
    }

    @Override
    public List<Contact> findAll() {
        return List.copyOf(values.values());
    }

    @Override
    public void save(Contact contact) {
        values.put(contact.getId(), contact); // <.>
    }

    // tag::findAllWithNameContaining[]
    @Override
    public Page<Contact> findAllWithNameContaining(String query,
                                                   int page,
                                                   int size) {
        List<Contact> unpaged = values.values()
                .stream()
                .filter(contact -> contact.hasName(query))
                .toList();
        List<Contact> contacts = unpaged.stream()
                .sorted(Comparator.comparing(contact -> contact.getGivenName() + " " + contact.getFamilyName()))
                .skip((long) page * size)
                .limit(size)
                .toList();
        return new Page<>(contacts,
                page,
                size,
                unpaged.size());
    }
    // end::findAllWithNameContaining[]

    // tag::findAllOrderedByName[]
    @Override
    public Page<Contact> findAllOrderedByName(int page, int size) {
        List<Contact> contacts = values.values().stream()
                .sorted(Comparator.comparing(contact -> contact.getGivenName() + " " + contact.getFamilyName()))
                .skip((long) page * size)
                .limit(size)
                .toList();
        return new Page<>(contacts,
                page,
                size,
                values.size());
    }
    // end::findAllOrderedByName[]

    // tag::findById[]
    @Override
    public Optional<Contact> findById(ContactId contactId) {
        return Optional.ofNullable(values.get(contactId));
    }
    // end::findById[]


    // tag::deleteById[]
    @Override
    public void deleteById(ContactId contactId) {
        values.remove(contactId);
    }
    // end::deleteById[]

    // tag::existsByEmail[]
    @Override
    public boolean existsByEmail(String email) {
        return values.values()
                .stream()
                .anyMatch(contact -> contact.getEmail().equals(email));
    }
    // end::existsByEmail[]

}
