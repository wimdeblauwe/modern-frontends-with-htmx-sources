package com.modernfrontendshtmx.contactsapp.contact;

import java.util.Locale;

public class Contact {
    private final ContactId id;
    private String givenName;
    private String familyName;
    private String phone;
    private String email;

    public Contact(ContactId id,
                   String givenName,
                   String familyName,
                   String phone,
                   String email) {
        this.id = id;
        this.givenName = givenName;
        this.familyName = familyName;
        this.phone = phone;
        this.email = email;
    }

    public ContactId getId() {
        return id;
    }

    public String getGivenName() {
        return givenName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean hasName(String name) {
        return givenName.toLowerCase(Locale.ROOT).contains(name)
                || familyName.toLowerCase(Locale.ROOT).contains(name);
    }
}
