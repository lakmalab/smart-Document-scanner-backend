package edu.icet.ecom.service.impl;

import com.azure.ai.inference.ChatCompletionsClient;
import com.azure.ai.inference.ChatCompletionsClientBuilder;
import com.azure.ai.inference.models.ChatCompletionsOptions;
import com.azure.ai.inference.models.ChatRequestMessage;
import com.azure.ai.inference.models.ChatRequestUserMessage;
import com.azure.core.credential.AzureKeyCredential;
import edu.icet.ecom.service.AIService;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class AIServiceImpl implements AIService {

    private String apiKey;
    private static final String ENDPOINT = "https://models.github.ai/inference";
    private static final String MODEL_NAME = "openai/gpt-4.1";
    private ChatCompletionsClient client;

    @Override
    public ChatCompletionsClient getClient() {
        if (client == null) {
            client = new ChatCompletionsClientBuilder()
                    .credential(new AzureKeyCredential(apiKey))
                    .endpoint(ENDPOINT)
                    .buildClient();
        }
        return client;
    }
    @Override
    public void setApiKey(String apikey) {
        this.apiKey = apikey;
        this.client = null; // reset for new credential
    }
    @Override
    public Map<String, String> extractFieldsFromText(String rawText, String prompt) {
        try {
            String fullPrompt = prompt + "\n\nText:\n" + rawText;

            List<ChatRequestMessage> messages = List.of(new ChatRequestUserMessage(fullPrompt));
            ChatCompletionsOptions options = new ChatCompletionsOptions(messages).setModel(MODEL_NAME);

            String response = getClient()
                    .complete(options)
                    .getChoices()
                    .get(0)
                    .getMessage()
                    .getContent();

            System.out.println("AI Response: " + response);

            // Parse JSON response
            JSONObject json = new JSONObject(response);
            Map<String, String> result = new LinkedHashMap<>();
            for (String key : json.keySet()) {
                result.put(key, json.getString(key));
            }
            return result;

        } catch (Exception e) {
            System.err.println("AI error: " + e.getMessage());
            return Map.of("error", "Exception: " + e.getMessage());
        }
    }
}
