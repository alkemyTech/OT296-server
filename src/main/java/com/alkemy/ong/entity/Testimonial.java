package com.alkemy.ong.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.lang.Nullable;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "testimonials")
@SQLDelete(sql = "UPDATE testimonials SET soft_delete = true WHERE id=?")
@Where(clause = "soft_delete=false")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Testimonial {

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    private String id;

    @Column(nullable = false)
    private String name;

    @Nullable
    private String image;

    @Nullable
    private String content;

    private LocalDateTime timestamps;

    private boolean softDelete = Boolean.FALSE;
}
