package com.scaler.userauthservice.controller;

import com.scaler.userauthservice.dto.AuthRequestDto;
import com.scaler.userauthservice.dto.UserDto;
import com.scaler.userauthservice.model.User;
import com.scaler.userauthservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping
    public ResponseEntity<UserDto> signUp(@RequestBody AuthRequestDto authRequestDto)
    {
        User user = authService.signUp(authRequestDto.getEmail(), authRequestDto.getPassword());
        if(user == null)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        UserDto userDto = new UserDto();
        userDto.setEmail(user.getEmail());
        userDto.setId(user.getId());
        userDto.setRole(user.getRole());

        return new ResponseEntity<>(userDto,HttpStatus.OK);
    }
}
