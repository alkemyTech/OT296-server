package com.alkemy.ong.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity(name = "News")
@Table(name = "news")
@SQLDelete(sql = "UPDATE news SET deleted = true WHERE id=?" )
@Where(clause = "deleted = false")
public class News {

    @Id
    @GeneratedValue
    @Column(
            name = "id",
            updatable = false
    )
    private UUID id;

    @Column(
            name = "name",
            nullable = false
    )
    private String name;

    @Column(
            name = "content",
            columnDefinition = "TEXT",
            nullable = false
    )
    private String content;

    @Column(
            name = "image",
            nullable = false
    )
    private String image;

    @CreationTimestamp
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createDateTime;

    @UpdateTimestamp
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime updateDateTime;

    private boolean deleted = Boolean.FALSE;

    @Column(name = "category_id_fk")
    private UUID categoryId;

}
