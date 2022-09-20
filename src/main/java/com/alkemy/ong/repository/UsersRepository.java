package com.alkemy.ong.repository;

import com.alkemy.ong.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<Users, String> {

}
