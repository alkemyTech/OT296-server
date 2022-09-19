package com.alkemy.ong.repository;

import com.alkemy.ong.entity.Slides;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SlidesRepository extends JpaRepository<Slides, UUID> {
}
