package com.alkemy.ong.repository;

import com.alkemy.ong.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository <Role, String> {
}
