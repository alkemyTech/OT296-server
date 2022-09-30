package com.alkemy.ong.repository;

import com.alkemy.ong.entity.Testimonial;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TestimonialRepository extends JpaRepository<Testimonial, String> {
}