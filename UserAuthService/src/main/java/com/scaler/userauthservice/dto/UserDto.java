package com.scaler.userauthservice.dto;

import com.scaler.userauthservice.model.Enum.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {

    private Long id;
    private String email;
    private Role role;
}
