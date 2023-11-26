package com.modernfrontendshtmx.contactsapp.contact.web;

import com.modernfrontendshtmx.contactsapp.contact.Contact;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class EditContactFormData {
    private long id;
    @NotBlank
    private String givenName;
    @NotBlank
    private String familyName;
    @NotBlank
    private String phone;
    @Email
    private String email;

    public static EditContactFormData from(Contact contact) { // <.>
        EditContactFormData formData = new EditContactFormData();
        formData.setId(contact.getId().value());
        formData.setGivenName(contact.getGivenName());
        formData.setFamilyName(contact.getFamilyName());
        formData.setPhone(contact.getPhone());
        formData.setEmail(contact.getEmail());

        return formData;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
