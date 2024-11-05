 package com.ad.dena_paona.service;

 import com.ad.dena_paona.entity.User;
 import com.ad.dena_paona.repository.UserRepository;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.stereotype.Service;

 import java.util.List;

 @Service
 public class UserService {
     @Autowired
     private UserRepository userRepository;
     public List<User> getAllUsers(){
         return userRepository.findAll();
     }
     public User getUserById(Long id){
         return userRepository.findById(id).orElse(null);
     }

     public User creteUser(User user){
         return userRepository.save(user);
     }

     public User updateUser(Long id, User userDetails){
         User user = getUserById(id);
         if(user != null){
             user.setUserName(userDetails.getUserName());
             user.setEmail(userDetails.getEmail());
             user.setPassword(userDetails.getPassword());
             return userRepository.save(user);
         }
         return null;
     }

     public String deleteUser(User user){
         userRepository.delete(user);
         return ("Deleted User : " + user.getUserName());
     }

     public String deleteUserById(Long id){
         userRepository.deleteById(id);
         return ("Deleted User : " + getUserById(id).getUserName());
     }
 }
