package com.scaler.productcatalogservice.service;

import com.scaler.productcatalogservice.clients.GroqClient;
import com.scaler.productcatalogservice.dto.GroqPitchResponseDto;
import com.scaler.productcatalogservice.model.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class PitchService {

    private static final String MODEL = "openai/gpt-oss-20b";
    private static final String SYSTEM_PROMPT = "You are a marketing copywriter for ShopNest, an online store. You write short push notifications that make people want to open the app.";

    private final GroqClient groqClient;

    public PitchService(GroqClient groqClient) {
        this.groqClient = groqClient;
    }

    public String generatePitch(Product product) {

        try {
            Map<String,Object> body = Map.of("model",MODEL,"reasoning_effort" ,"low","max_completion_tokens",300,
                    "messages", List.of(
                            Map.of("role","system","content",SYSTEM_PROMPT),
                            Map.of("role","user","content", buildPrompt(product))
                    )
            );

            GroqPitchResponseDto response = groqClient.chat(body);

            String pitch = response.getChoices().get(0).getMessage().getContent();
            return pitch;


        } catch (Exception e) {
            //System.out.println(e.getMessage());
            log.warn("Error in LLM call " + e.getMessage());
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
