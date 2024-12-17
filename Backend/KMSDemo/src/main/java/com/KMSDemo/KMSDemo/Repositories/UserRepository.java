package com.KMSDemo.KMSDemo.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.KMSDemo.KMSDemo.Entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
     User findByUsername(String username);
}
