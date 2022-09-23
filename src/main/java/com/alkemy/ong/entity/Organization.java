package com.alkemy.ong.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE organizations SET soft_delete = true WHERE id=?")
@Where(clause = "soft_delete=false")
@Table(name="organizations")
public class Organization {

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    private String id;

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
