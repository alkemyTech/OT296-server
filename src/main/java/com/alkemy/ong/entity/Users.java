package com.alkemy.ong.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "users")
@SQLDelete(sql = "UPDATE users SET softDelete = true WHERE id=?")
@Where(clause = "softDelete=false")
public class User {


    @Id @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    private String id;


    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    @Nullable
    private String photo;
    private LocalDateTime timestamps;
    private boolean softDelete = Boolean.FALSE;


    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "role_id", insertable = false,updatable = false)
    private Role role;

    @Column(name = "role_id")
    private String roleId;

}
