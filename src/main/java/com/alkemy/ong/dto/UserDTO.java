package com.alkemy.ong.dto;

import com.alkemy.ong.entity.Role;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class UserDTO {

    private String firstName;
    private String lastName;
    private String email;
    private String photo;
    private LocalDateTime timestamps;
    private Role role;
}