package com.alkemy.ong.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewsDTO {
    private String id;
    private String name;
    private String content;
    private String image;
    private LocalDateTime createDateTime;
    private LocalDateTime updateDateTime;
}
