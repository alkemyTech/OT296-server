package com.alkemy.ong.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "slides")
@Getter
@Setter
@SQLDelete(sql = "UPDATE slides SET deleted = true WHERE id=?")
@Where(clause = "deleted = false")
public class Slides {
    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    private String id;

    private String imageURL;
    private String text;
    @Column(name = "orders")
    private String order;

    private boolean deleted = Boolean.FALSE;

    @Column(name = "organization_id", nullable = false)
    private String organizationID;
}
