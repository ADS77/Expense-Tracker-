package com.ad.dena_paona.controller;

import com.ad.dena_paona.entity.Contact;
import com.ad.dena_paona.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/DP/contacts")
public class ContactsController {
    @Autowired
    private  ContactService contactService;
    @GetMapping("/get/{id}")
    public ResponseEntity<List<Contact>> getContactByUserId(@PathVariable Long id){
        List<Contact> contacts = contactService.getContactsByUserId(id);
        if(contacts.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(contacts);
    }
    @PostMapping("/create")
    public Contact createContact(@RequestBody Contact contact){
        return contactService.createContact(contact);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Contact> updateContact(@PathVariable Long id, @RequestBody Contact contact){
        Contact updatedContact = contactService.updateContact(id, contact);
        if(updatedContact == null){
            ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(contact);

    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteContact(@PathVariable Long id) {
        contactService.deleteContactById(id);
        return ResponseEntity.noContent().build();
    }

}
