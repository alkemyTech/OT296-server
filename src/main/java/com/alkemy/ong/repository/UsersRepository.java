package com.alkemy.ong.repository;

import com.alkemy.ong.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<Users, String> {

    Users findByEmailOrPassword(String email, String password);

    boolean existsByPassword(String password);

    @Query(value = "SELECT count(u) FROM USER AS u", nativeQuery = true)
    boolean existsByEmail(String email);
}
