package com.alkemy.ong.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "roles")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    private String id;

    @NotNull
    private String name;
    @Column(nullable = true)
    private String description;
    @Column(name = "timestamps")
    @Temporal(TemporalType.DATE)
    private Date timestamps;
    public Role(String name, String description) {
        this.name=name;
        this.description=description;
    }
    public Role get() {
    return this;
    }
}