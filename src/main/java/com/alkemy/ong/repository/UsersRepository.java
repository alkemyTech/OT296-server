package com.alkemy.ong.repository;

import com.alkemy.ong.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, String> {

    Users findByEmailOrPassword(String email, String password);

    boolean existsByPassword(String password);

    @Query(value = "SELECT count(u) FROM USER AS u", nativeQuery = true)
    boolean existsByEmail(String email);

    Optional<Users> findByEmail(String email);
}