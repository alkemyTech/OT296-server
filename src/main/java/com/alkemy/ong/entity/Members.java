package com.alkemy.ong.entity;

import com.sun.istack.Nullable;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@SQLDelete(sql = "UPDATE members SET soft_delete = true WHERE id=?")
@Where(clause = "soft_delete=false")
//@Builder
//@AllArgsConstructor
//@NoArgsConstructor
public class Members {

    @Id 
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    private String id;

    @Column(nullable = false)
    private String name;

    @Nullable
    private String facebookUrl;

    @Nullable
    private String instagramUrl;

    @Nullable
    private String linkedinUrl;

    @Column
    private String image;

    @Nullable
    private String description;

    private LocalDateTime timestamps;

    private boolean softDelete = Boolean.FALSE;

}
