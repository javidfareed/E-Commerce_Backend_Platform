package com.scaler.productcatalogservice.security;

import com.scaler.productcatalogservice.dto.AuthUserDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.restclient.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class TokenValidationFilter extends OncePerRequestFilter {

    //We need the url to communicate UserAuth microservice to validate token
    private final String authServiceUrl;
    private final RestTemplate restTemplate;

    public TokenValidationFilter(@Value("${auth.service.url}") String authServiceUrl, RestTemplateBuilder restTemplateBuilder) {
        this.authServiceUrl = authServiceUrl;
        this.restTemplate = restTemplateBuilder.build();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        if(request.getMethod().equals("GET"))
        {
           filterChain.doFilter(request,response);
           return;
        }

        String header = request.getHeader("Authorization");
        if(header == null || !header.startsWith("Bearer "))
        {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }

        //validate token for requests other than GET
        AuthUserDto authUserDto;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", header);
            ResponseEntity<AuthUserDto> answer = restTemplate.exchange(
                    authServiceUrl+"/auth/validateToken",
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    AuthUserDto.class);
            authUserDto = answer.getBody();
        }
        catch(HttpClientErrorException.Unauthorized e)
        {
           response.setStatus(HttpStatus.UNAUTHORIZED.value());
           return;
        }
        catch(RestClientException e)
        {
            response.setStatus(HttpStatus.SERVICE_UNAVAILABLE.value());
            return;
        }

        if(request.getMethod().equals("DELETE") && !"ADMIN".equals(authUserDto.getRole()))
        {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            return;
        }

       filterChain.doFilter(request,response);
    }
}
