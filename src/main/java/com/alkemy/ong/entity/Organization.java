package com.alkemy.ong.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="organizations")
public class Organization {

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
    private boolean softDelete = Boolean.FALSE;

}
