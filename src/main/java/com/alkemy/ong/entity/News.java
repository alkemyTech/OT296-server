package com.alkemy.ong.entity;

import lombok.*;
import org.hibernate.annotations.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "News")
@Table(name = "news")
@SQLDelete(sql = "UPDATE news SET deleted = true WHERE id=?" )
@Where(clause = "deleted = false")
public class News {

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    private String id;

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
    @Column(
            name = "create_timestamp",
            updatable = false
    )
    private LocalDateTime createDateTime;

    @UpdateTimestamp
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(
            name = "update_timestamp"
    )
    private LocalDateTime updateDateTime;

    private boolean deleted = Boolean.FALSE;


    @OneToOne(
            targetEntity = Category.class,
            cascade = {CascadeType.PERSIST}
    )
    @JoinColumn(
            name = "category_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(
                    name = "category_id_fk"
            )
    )
    private Category categoryId;

}
