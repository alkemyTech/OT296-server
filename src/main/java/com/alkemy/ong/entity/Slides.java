package com.alkemy.ong.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "slides")
@Getter
@Setter
public class Slides {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private UUID id;

    private String imageURL;
    private String text;
    @Column(name = "orderS")
    private String order;

    @Column(name = "organization_id", nullable = false)
    private UUID organizationID;
}
