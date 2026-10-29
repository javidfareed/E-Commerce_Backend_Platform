package com.scaler.userauthservice.controller;

import com.scaler.userauthservice.dto.AuthRequestDto;
import com.scaler.userauthservice.dto.LoginResponseDto;
import com.scaler.userauthservice.dto.UserDto;
import com.scaler.userauthservice.model.Enum.Role;
import com.scaler.userauthservice.model.User;
import com.scaler.userauthservice.service.AuthService;
import com.scaler.userauthservice.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    @PostMapping("/signup")
    public ResponseEntity<UserDto> signUp(@Valid @RequestBody AuthRequestDto authRequestDto)
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

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody AuthRequestDto authRequestDto)
    {
        String token = authService.login(authRequestDto.getEmail(),authRequestDto.getPassword());

        if(token == null)
        {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        LoginResponseDto responseDto = new LoginResponseDto();
        responseDto.setToken(token);

        return new ResponseEntity<>(responseDto,HttpStatus.OK);
    }

    @GetMapping("/validateToken")
    public ResponseEntity<UserDto> validateToken(@RequestHeader("Authorization") String header)
    {
        if(!header.startsWith("Bearer "))
        {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        try {
            Claims claims = jwtService.parseToken(header.substring("Bearer ".length()));
            UserDto userDto = new UserDto();
            userDto.setEmail(claims.getSubject());
            userDto.setRole(Role.valueOf(claims.get("role", String.class)));
            userDto.setId(Long.parseLong(claims.get("userId", String.class)));

            return new ResponseEntity<>(userDto,HttpStatus.OK);
        }
        catch (JwtException e)
        {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

    }
}
