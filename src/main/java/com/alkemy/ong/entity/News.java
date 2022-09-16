package com.alkemy.ong.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity(name = "News")
@Table(name = "news")
@SQLDelete(sql = "UPDATE news SET deleted = true WHERE id=?" )
@Where(clause = "deleted=false")
public class News {

    @Id
    @GeneratedValue
    @Column(
            name = "id",
            updatable = false
    )
    private UUID id;

    @Column(
            name = "name"
    )
    @NotNull
    private String name;

    @Column(
            name = "content",
            columnDefinition = "TEXT"
    )
    @NotNull
    private String content;

    @Column(
            name = "image"
    )
    @NotNull
    private String image;

    @CreationTimestamp
    private LocalDateTime createDateTime;

    @UpdateTimestamp
    private LocalDateTime updateDateTime;

    private boolean deleted = Boolean.FALSE;

    @OneToOne
    @JoinColumn(
            name = "category_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(
                    name = "category_id_fk"
            )
    )
    private UUID categoryId;

}
