package com.alkemy.ong.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Builder
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE contacts SET deleted_at = true WHERE id=?")
@Where(clause = "deleted_at = false")
@Table(name="contacts")
public class Contact {

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    private String id;

    @Column(nullable = false)
    private String name;
    private String phone;
    @Column(nullable = false)
    private String email;
    private String message;
    private boolean deletedAt = Boolean.FALSE;

}
