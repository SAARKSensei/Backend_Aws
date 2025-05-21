package com.sensei.backend.service;

import com.sensei.backend.entity.ContactUs;
import com.sensei.backend.error.UserNotFoundException;
import com.sensei.backend.repository.ContactUsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ContactUsImpl implements ContactUsService{

    @Autowired
    private ContactUsRepository contactUsRepository;
    @Override
    public ContactUs saveUser(ContactUs user) {
        return contactUsRepository.save(user);
    }

    @Override
    public List<ContactUs> fetchUserList() {
        return contactUsRepository.findAll();
    }

    @Override
    public ContactUs fetchUserById(Long userId) throws UserNotFoundException {
        Optional<ContactUs> user = contactUsRepository.findById(userId);
        if(!user.isPresent()){
            throw new UserNotFoundException("User Not Available");
        }
        return user.get();
    }

    @Override
    public void deleteUserById(Long userId) {
        contactUsRepository.deleteById(userId);
    }

    @Override
    public ContactUs updateUser(Long userId, ContactUs user) {
        ContactUs userDB = contactUsRepository.findById(userId).get();
        if(Objects.nonNull(user.getUserName()) && !"".equalsIgnoreCase(user.getUserName())){
            userDB.setUserName(user.getUserName());
        }

        if(Objects.nonNull(user.getEmail()) && !"".equalsIgnoreCase(user.getEmail())){
            userDB.setEmail(user.getEmail());
        }

        if(Objects.nonNull(user.getPhoneNumber()) && !"".equalsIgnoreCase(user.getPhoneNumber())){
            userDB.setPhoneNumber(user.getPhoneNumber());
        }
        return contactUsRepository.save(userDB);
    }

    @Override
    public ContactUs fetchUserByName(String userName) {
        return contactUsRepository.findByUserNameIgnoreCase(userName);
    }
}