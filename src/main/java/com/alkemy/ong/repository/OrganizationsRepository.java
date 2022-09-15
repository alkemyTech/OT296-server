package com.alkemy.ong.repository;

import com.alkemy.ong.entity.Organizations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrganizationsRepository extends JpaRepository<Organizations, UUID> {
}
