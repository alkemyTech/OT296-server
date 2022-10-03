package com.alkemy.ong.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Optional;

@Entity
@Getter
@Setter
@Table(name = "users")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "UPDATE users SET soft_delete = true WHERE id=?")
@Where(clause = "soft_delete=false")
public class Users {

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
    @Column(name = "PHOTO")
    private String photo;
    private LocalDateTime timestamps;
    private boolean softDelete = Boolean.FALSE;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", updatable = false)
    private Role role;
    public Users(String firstName, String lastName, String email, String password, String photo, Role role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password =password;
        this.photo = photo;
        this.role= role;
    }
}
