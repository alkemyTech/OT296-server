package com.alkemy.ong.entity;


import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;


import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "categories")
@Getter
@Setter
@SQLDelete(sql = "UPDATE categories SET soft_delete = true WHERE id=?")
@Where(clause = "soft_delete = false")
public class Category {

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    private String id;

    @Column(nullable = false)
    private  String name;

    private String description,image;

    private LocalDate timestamps;

    private boolean softDelete = Boolean.FALSE;
}
