package com.scaler.productcatalogservice.service;

import com.scaler.productcatalogservice.dto.GroqPitchResponseDto;
import com.scaler.productcatalogservice.model.Product;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.restclient.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class PitchService {

    private static final String GROQ_URL = "https://api.groq.com/openai/v1/chat/completions";
    private static final String MODEL = "openai/gpt-oss-20b";
    private static final String SYSTEM_PROMPT = "You are a marketing copywriter for ShopNest, an online store. You write short push notifications that make people want to open the app.";

    private final RestTemplate restTemplate;
    private final String apiKey;

    public PitchService(RestTemplateBuilder restTemplateBuilder,@Value("${groq.api-key}") String apiKey) {
        this.restTemplate = restTemplateBuilder.build();
        this.apiKey = apiKey;
    }

    public String generatePitch(Product product) {

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        try {
            Map<String,Object> body = Map.of("model",MODEL,"reasoning_effort" ,"low","max_completion_tokens",300,
                    "messages", List.of(
                            Map.of("role","system","content",SYSTEM_PROMPT),
                            Map.of("role","user","content", buildPrompt(product))
                    )
            );

            ResponseEntity<GroqPitchResponseDto> response = restTemplate.postForEntity(GROQ_URL,new HttpEntity<>(body,headers), GroqPitchResponseDto.class);
            if(response.getStatusCode().is2xxSuccessful() && response.hasBody())
            {
                return response.getBody().getChoices().get(0).getMessage().getContent();
            }

            throw new IllegalArgumentException("Pitch couldn't be generated");


        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "New arrival: " + product.getName() +
                    " for just $" + product.getPrice() + ". Check it out!";
        }

    }

    private String buildPrompt(Product product) {

        return """
                Write ONE push notification for this product.
                Rules:
                - Maximum 25 words.
                - Mention the price.
                - At most one emoji.
                - No hashtags.
                - Reply with only the notification text.

                Product: %s
                Category: %s
                Price: $%s
                Details: %s""".formatted(product.getName(),product.getCategory().getName(),product.getPrice(),
                product.getDescription());
    }
}
