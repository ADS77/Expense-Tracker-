package com.ad.dena_paona.controller;

import com.ad.dena_paona.entity.Contact;
import com.ad.dena_paona.service.ContactService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/DP/contacts")
public class ContactsController {

    private final ContactService contactService;

    public ContactsController(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping("/{id}")
    public List<Contact> getContactsByUserId(@PathVariable Long id){
        return contactService.getContactsByUserId(id);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Contact> getContactByUserId(@PathVariable Long id){
        Contact contact = contactService.getContactByUserId(id);
        if(contact == null){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(contact);
    }
    @PostMapping
    public Contact createContact(@RequestBody Contact contact){
        return contactService.createContact(contact);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Contact> updateContact(@PathVariable Long id, @RequestBody Contact contact){
        Contact updatedContact = contactService.updateContact(id, contact);
        if(updatedContact == null){
            ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(contact);

    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContact(@PathVariable Long id) {
        contactService.deleteContactById(id);
        return ResponseEntity.noContent().build();
    }

}
