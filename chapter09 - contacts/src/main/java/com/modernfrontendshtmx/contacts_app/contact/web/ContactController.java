package com.modernfrontendshtmx.contacts_app.contact.web;

import com.modernfrontendshtmx.contacts_app.contact.Contact;
import com.modernfrontendshtmx.contacts_app.contact.ContactId;
import com.modernfrontendshtmx.contacts_app.contact.service.ArchiveId;
import com.modernfrontendshtmx.contacts_app.contact.service.ArchiveProcessInfo;
import com.modernfrontendshtmx.contacts_app.contact.service.Archiver;
import com.modernfrontendshtmx.contacts_app.contact.service.ContactService;
import com.modernfrontendshtmx.contacts_app.infrastructure.repository.Page;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxRequest;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Controller
@RequestMapping("/contacts")
public class ContactController {
    private final ContactService service;
    private final Archiver archiver;

    public ContactController(ContactService service, Archiver archiver) {
        this.service = service;
        this.archiver = archiver;
    }

    // tag::viewContacts[]
    @GetMapping
    public String viewContacts(Model model,
                               @RequestParam(value = "q", required = false) String query,
                               @RequestParam(value = "page", required = false, defaultValue = "0") int page,
                               HtmxRequest htmxRequest) {
        Page<Contact> contactsPage;
        if (query != null) {
            model.addAttribute("query", query);
            contactsPage = service.searchContacts(query, page);
        } else {
            contactsPage = service.getAll(page);
        }
        model.addAttribute("page", contactsPage.number());
        model.addAttribute("size", contactsPage.size());
        model.addAttribute("totalElements", contactsPage.totalElements());
        model.addAttribute("contacts", contactsPage.values());

        if (htmxRequest.isHtmxRequest()
                && !"delete-button".equals(htmxRequest.getSourceElementId())) { // <.>
            return "contacts/list :: tbody";
        } else {
            return "contacts/list";
        }
    }
    // end::viewContacts[]

    // tag::newContact[]
    @GetMapping("/new")
    public String newContact(Model model) {
        model.addAttribute("formData", new CreateContactFormData());
        model.addAttribute("editMode", EditMode.CREATE); // <.>

        return "contacts/edit";
    }
    // end::newContact[]

    // tag::createNewContact[]
    @PostMapping("/new")
    public String createNewContact(Model model,
                                   @ModelAttribute("formData") @Valid CreateContactFormData formData,
                                   BindingResult bindingResult,
                                   RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("editMode", EditMode.CREATE); // <.>
            return "contacts/edit";
        }

        service.storeNewContact(formData.getGivenName(),
                formData.getFamilyName(),
                formData.getPhone(),
                formData.getEmail());

        redirectAttributes.addFlashAttribute("successMessage",
                "Created New Contact!");

        return "redirect:/contacts";
    }
    // end::createNewContact[]

    // tag::validateNewContact[]
    @PostMapping("/validate") // <.>
    @HxRequest // <.>
    public String validateNewContact(Model model,
                                     @ModelAttribute("formData") @Valid CreateContactFormData formData, // <.>
                                     BindingResult bindingResult) { // <.>
        model.addAttribute("formData", formData);
        model.addAttribute("editMode", EditMode.CREATE);
        return "contacts/edit"; // <.>
    }
    // end::validateNewContact[]

    // tag::viewContact[]
    @GetMapping("/{id}")
    public String viewContact(Model model,
                              @PathVariable("id") long id) {
        Contact contact = service.getContact(new ContactId(id));
        model.addAttribute("contact", contact);

        return "contacts/view";
    }
    // end::viewContact[]

    // tag::editContact[]
    @GetMapping("/{id}/edit")
    public String editContact(Model model,
                              @PathVariable("id") long id) { // <.>
        Contact contact = service.getContact(new ContactId(id));
        model.addAttribute("formData", EditContactFormData.from(contact));
        model.addAttribute("editMode", EditMode.UPDATE);

        return "contacts/edit";
    }

    @PostMapping("/{id}/edit")
    public String doEditContact(Model model,
                                @PathVariable("id") long id,
                                @ModelAttribute("formData") @Valid EditContactFormData formData,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes) { // <.>
        if (bindingResult.hasErrors()) {
            model.addAttribute("editMode", EditMode.UPDATE);
            return "contacts/edit";
        }

        service.updateContact(new ContactId(id),
                formData.getGivenName(),
                formData.getFamilyName(),
                formData.getPhone(),
                formData.getEmail());

        redirectAttributes.addFlashAttribute("successMessage",
                "Updated Contact!");
        return "redirect:/contacts";
    }
    // end::editContact[]

    // tag::deleteContact[]
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContact(@PathVariable("id") long id,
                                              RedirectAttributes redirectAttributes,
                                              HtmxRequest htmxRequest) { // <.>
        service.deleteContact(new ContactId(id));

        if ("delete-button".equals(htmxRequest.getSourceElementId())) { // <.>
            redirectAttributes.addFlashAttribute("successMessage",
                    "Deleted Contact!");

            return ResponseEntity.status(HttpStatus.SEE_OTHER)
                    .location(URI.create("/contacts"))
                    .build(); // <.>
        } else {
            return ResponseEntity.ok().build(); // <.>
        }
    }
    // end::deleteContact[]

    // tag::createArchive[]
    @PostMapping("/archives")
    @HxRequest
    public String createArchive(Model model) {
        ArchiveId archiveId = archiver.startArchiving();
        ArchiveProcessInfo processInfo = archiver.getArchiveProcessInfo(archiveId);
        model.addAttribute("archiveId", archiveId.value());
        model.addAttribute("status", processInfo.getStatus());
        model.addAttribute("progress", processInfo.getProgress());
        return "contacts/archive";
    }
    // end::createArchive[]

    // tag::getArchive[]
    @GetMapping("/archives/{id}")
    @HxRequest
    public String getArchive(Model model,
                             @PathVariable("id") UUID id) {
        ArchiveId archiveId = new ArchiveId(id);
        ArchiveProcessInfo processInfo = archiver.getArchiveProcessInfo(archiveId);
        model.addAttribute("archiveId", archiveId.value());
        model.addAttribute("status", processInfo.getStatus());
        model.addAttribute("progress", processInfo.getProgress());
        return "contacts/archive";
    }
    // end::getArchive[]

    // tag::downloadArchive[]
    @GetMapping("/archives/{id}")
    public void downloadArchive(@PathVariable("id") UUID id,
                                HttpServletResponse response) throws ExecutionException, InterruptedException, IOException {
        ArchiveId archiveId = new ArchiveId(id);
        ArchiveProcessInfo processInfo = archiver.getArchiveProcessInfo(archiveId);
        String archive = processInfo.getFuture().get();

        ContentDisposition contentDisposition = ContentDisposition
                .attachment()
                .filename("archive.csv", StandardCharsets.UTF_8)
                .build();

        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString());
        response.getOutputStream().write(archive.getBytes(StandardCharsets.UTF_8));
        response.flushBuffer();
    }
    // end::downloadArchive[]
}
