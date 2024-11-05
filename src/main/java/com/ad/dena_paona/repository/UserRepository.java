 package com.ad.dena_paona.repository;

 import com.ad.dena_paona.entity.User;
 import org.springframework.data.jpa.repository.JpaRepository;
 import org.springframework.stereotype.Repository;

 @Repository
 public interface UserRepository extends JpaRepository<User,Long> {
     User findByUserName(String userName);
     User findByEmail(String email);
 }
