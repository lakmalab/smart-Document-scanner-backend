package edu.icet.ecom.service.impl;

import com.azure.ai.inference.ChatCompletionsClient;
import com.azure.ai.inference.ChatCompletionsClientBuilder;
import com.azure.ai.inference.models.ChatCompletionsOptions;
import com.azure.ai.inference.models.ChatRequestMessage;
import com.azure.ai.inference.models.ChatRequestUserMessage;
import com.azure.core.credential.AzureKeyCredential;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AIService {

    @Value("${githubai.api.key}")
    private String apiKey;

    private static final String ENDPOINT = "https://models.github.ai/inference";
    private static final String MODEL_NAME = "openai/gpt-4.1";

    private ChatCompletionsClient client;

    // Lazy initialize once
    private ChatCompletionsClient getClient() {
        if (client == null) {
            client = new ChatCompletionsClientBuilder()
                    .credential(new AzureKeyCredential(apiKey))
                    .endpoint(ENDPOINT)
                    .buildClient();
        }
        return client;
    }

    public String extractValue(String rawText, String prompt) {
        try {
            String fullPrompt = prompt + "\n\nOCR Text:\n" + rawText;

            List<ChatRequestMessage> messages = new ArrayList<>();
            messages.add(new ChatRequestUserMessage(fullPrompt));

            ChatCompletionsOptions options = new ChatCompletionsOptions(messages);
            options.setModel(MODEL_NAME);

            String response = getClient()
                    .complete(options)
                    .getChoices()
                    .get(0)
                    .getMessage()
                    .getContent();

            System.out.println("AI Response: " + response);
            return response;

        } catch (Exception e) {
            System.err.println("Error during inference: " + e.getMessage());
            return "{\"error\": \"Exception\", \"message\": \"" + e.getMessage() + "\"}";
        }
    }

    public List<Pair<String, String>> extractJsonEntities(String rawText, String prompt) {
        String jsonResponse = extractValue(rawText, prompt);
        List<Pair<String, String>> entities = new ArrayList<>();
        try {
            JSONObject json = new JSONObject(jsonResponse);
            for (String key : json.keySet()) {
                String value = json.getString(key);
                entities.add(new Pair<>(value, key));
            }
        } catch (Exception e) {
            System.err.println("JSON parsing error: " + e.getMessage());
        }
        return entities;
    }

    public record Pair<A, B>(A first, B second) {}
}
