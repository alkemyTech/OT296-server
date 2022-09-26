package com.alkemy.ong.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity(name = "Activity")
@Table(name = "activities")
@SQLDelete(sql = "UPDATE activity SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
public class Activity {

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    private String id;

    @Column(name = "name")
    @NotNull
    private String name;

    @Column(
            name = "content",
            columnDefinition = "TEXT"
    )
    @NotNull
    private String content;

    @Column(name = "image")
    @NotNull
    private String image;

    private boolean deleted = Boolean.FALSE;

    @CreationTimestamp
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(
            name = "create_timestamp",
            updatable = false
    )
    private LocalDateTime createDateTime;

    @UpdateTimestamp
    @Column(
            name = "update_timestamp"
    )
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime updateDateTime;

}
