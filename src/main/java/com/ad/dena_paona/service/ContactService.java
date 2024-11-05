package com.ad.dena_paona.service;

import com.ad.dena_paona.entity.Contact;
import com.ad.dena_paona.repository.ContactRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ContactService {
    @Autowired
    private ContactRepository contactRepository;
    private static final Logger logger = LoggerFactory.getLogger(ContactService.class);

    public List<Contact> getContactsByUserId(Long userId){
        return  contactRepository.findByUser_UserId(userId);
    }

    public Contact getContactByUserId(Long userId){
        return contactRepository.findById(userId).orElse(null);
    }

    public Contact createContact(Contact contact){
        logger.info("contact being saved :", contact.getName());
        return contactRepository.save(contact);
    }

    public Contact updateContact(Long id, Contact contactDetails){
        Contact contact = getContactByUserId(id);
        if(contact != null){
            contact.setName(contactDetails.getName());
            contact.setEmail(contactDetails.getEmail());
            contact.setPhoneNo(contactDetails.getPhoneNo());
            contact.setUpdatedAt(LocalDateTime.now());
            return contactRepository.save(contact);
        }
        return null;
    }

    public void delete(Contact contact){
        contactRepository.delete(contact);
    }

    public void deleteContactById(Long id){
        contactRepository.deleteById(id);
    }
}
