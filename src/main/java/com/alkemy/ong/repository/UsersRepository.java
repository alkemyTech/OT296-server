package com.alkemy.ong.repository;

import com.alkemy.ong.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UsersRepository extends JpaRepository<Users, UUID> {

    Users findByEmail(String email);
}
