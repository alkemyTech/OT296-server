package com.alkemy.ong.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name="organizations")
public class Organizations {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private UUID id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String image;
    private String address;
    private Integer phone;
    @Column(nullable = false)
    private String email;
    private String welcomeText;
    private String aboutUsText;
    private LocalDateTime timestamps;
    private boolean softDelete;

    public Organizations() {}

    public Organizations(String name, String image, String address, Integer phone, String email, String welcomeText, String aboutUsText, LocalDateTime timestamps, boolean softDelete) {
        this.name = name;
        this.image = image;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.welcomeText = welcomeText;
        this.aboutUsText = aboutUsText;
        this.timestamps = timestamps;
        this.softDelete = false;
    }

}
