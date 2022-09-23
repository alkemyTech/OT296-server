package com.alkemy.ong.repository;

import com.alkemy.ong.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRepository extends JpaRepository<Contact, String> {
    boolean existsByEmail(String email);
}
