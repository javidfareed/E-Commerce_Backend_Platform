package com.scaler.productcatalogservice.clients;

import com.scaler.productcatalogservice.dto.AuthUserDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class AuthClient {

    private final RestClient restClient;

    public AuthClient(RestClient.Builder builder, @Value("${auth.service.url}") String authServiceUrl) {
        this.restClient = builder.baseUrl(authServiceUrl).build();
    }


    public AuthUserDto validateToken(String authorizationHeader)
    {
        return restClient.get()
                .uri("/auth/validateToken")
                .header("Authorization" , authorizationHeader)
                .retrieve()
                .body(AuthUserDto.class);
    }
}
