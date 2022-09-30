package com.alkemy.ong.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "comments")
@SQLDelete(sql = "UPDATE comments SET soft_delete = true WHERE id=?")
@Where(clause = "soft_delete = false")
public class Comment {

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    private String id;

    private String body;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", updatable = false)
    private Users user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "news_id", updatable = false)
    private News news;

    private boolean deleted = Boolean.FALSE;

}