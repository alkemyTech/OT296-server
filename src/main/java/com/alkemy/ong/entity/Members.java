package com.alkemy.ong.entity;

import com.sun.istack.Nullable;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@SQLDelete(sql = "UPDATE users SET softDelete = true WHERE id=?")
@Where(clause = "softDelete=false")
public class Members {

    @Id @GeneratedValue(generator="system-uuid")
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

    @Column(nullable = false)
    private String image;

    @Nullable
    private String description;

    private LocalDateTime timestamps;

    private boolean softDelete = Boolean.FALSE;

}
