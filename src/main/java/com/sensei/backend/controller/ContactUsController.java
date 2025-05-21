package com.sensei.backend.controller;

import com.sensei.backend.entity.ContactUs;
import com.sensei.backend.error.UserNotFoundException;
import com.sensei.backend.service.ContactUsService;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ContactUsController {

    @Autowired
    private ContactUsService contactUsService;

    private final Logger LOGGER = LoggerFactory.getLogger(ContactUsController.class);

    @PostMapping("/api/contact-us")
    public ContactUs saveUser(@Valid @RequestBody ContactUs user){
        LOGGER.info("Inside saveUser of UserController");
        return contactUsService.saveUser(user);
    }

    @GetMapping("/api/contact-us")
    public List<ContactUs> fetchUserList(){
        LOGGER.info("Inside fetchUserList of UserController");
        return contactUsService.fetchUserList();
    }

    @GetMapping("/api/contact-us/{id}")
    public ContactUs fetchUserById(@PathVariable("id") Long userId) throws UserNotFoundException {
        return contactUsService.fetchUserById(userId);
    }

    @DeleteMapping("/api/contact-us/{id}")
    public String deleteUserById(@PathVariable("id") Long userId){
        contactUsService.deleteUserById(userId);
        return "User deleted Successfully";
    }

    @PutMapping("/api/contact-us/{id}")
    public ContactUs updateUser(@PathVariable("id") Long userId, @RequestBody ContactUs user){
        return contactUsService.updateUser(userId, user);
    }

    @GetMapping("/api/contact-us/name/{name}")
    public ContactUs fetchUserByName(@PathVariable("name") String userName){
        return contactUsService.fetchUserByName(userName);
    }

}