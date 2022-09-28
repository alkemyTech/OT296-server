package com.alkemy.ong.aws.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity
@Getter
@Setter
public class Image {

    @Id
    @GeneratedValue
    private Integer id;
    private String title;
    private String imagePath;
    @Transient
    private String imageUrl;
}
