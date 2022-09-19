package com.alkemy.ong.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.lang.Nullable;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "testimonials")
@SQLDelete(sql = "UPDATE testimonials SET softDelete = true WHERE id=?")
@Where(clause = "softDelete=false")
public class Testimonial {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Nullable
    private String image;

    @Nullable
    private String content;

    private LocalDateTime timestamps;

    private boolean softDelete = Boolean.FALSE;
}
