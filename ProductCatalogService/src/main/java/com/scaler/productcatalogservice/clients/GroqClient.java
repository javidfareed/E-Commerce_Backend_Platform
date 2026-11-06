package com.scaler.productcatalogservice.clients;

import com.scaler.productcatalogservice.dto.GroqPitchResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Component
public class GroqClient {

    private final RestClient restClient;

    public GroqClient(RestClient.Builder builder, @Value("${groq.url}") String url, @Value("${groq.api-key}") String apiKey){

        this.restClient = builder
                .baseUrl(url)
                .defaultHeaders(httpHeaders -> httpHeaders.setBearerAuth(apiKey))
                .build();

    }

    public GroqPitchResponseDto chat(Map<String , Object> body)
    {
        return restClient.post()
                .body(body)
                .retrieve()
                .body(GroqPitchResponseDto.class);
    }
}
