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
@SQLDelete(sql = "UPDATE comments SET deleted = true WHERE id=?")
@Where(clause = "deleted = false")
public class Comment {

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    private String id;

    private String body;

    @OneToOne (fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", updatable = false, insertable = false)
    private Users user;

    @Column(name = "user_id", nullable = false)
    private String userID;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "news_id", updatable = false, insertable = false)
    private News news;

    @Column(name = "news_id", nullable = false)
    private String newsID;

    private boolean deleted = Boolean.FALSE;

}