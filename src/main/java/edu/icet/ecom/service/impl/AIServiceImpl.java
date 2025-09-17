package edu.icet.ecom.service.impl;

import com.azure.ai.inference.ChatCompletionsClient;
import edu.icet.ecom.service.AIService;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Executors;

@Service
public class AIServiceImpl implements AIService {

    private String apiKey;
    private String model;
    private final OkHttpClient client = new OkHttpClient();
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final String GITHUB_ENDPOINT = "https://models.inference.ai.azure.com/chat/completions";

    public AIServiceImpl() {

    }

    @Override
    public ChatCompletionsClient getClient() {
        return null;
    }

    @Override
    public void setApiKey(String apiKey,String model) {
        this.apiKey = apiKey;
        this.model = model;
    }

    @Override
    public Map<String, String> extractFieldsFromText(String rawText, String prompt) {
        if (apiKey == null || apiKey.isEmpty()) {
            return Map.of("error", "API key not set");
        }

        try {
            String fullPrompt = prompt + "\n\nText:\n" + rawText;

            JSONObject jsonBody = new JSONObject();
            jsonBody.put("model", model);
            jsonBody.put("messages", new JSONArray().put(
                    new JSONObject().put("role", "user").put("content", fullPrompt)
            ));
            jsonBody.put("temperature", 0.1);

            Request request = new Request.Builder()
                    .url(GITHUB_ENDPOINT)
                    .post(RequestBody.create(JSON, jsonBody.toString()))
                    .addHeader("Authorization", "Bearer " + apiKey)
                    .addHeader("Content-Type", "application/json")
                    .build();

            Response response = client.newCall(request).execute();

            if (!response.isSuccessful()) {
                String errorBody = response.body() != null ? response.body().string() : "";
                return Map.of("error", "HTTP " + response.code() + ": " + errorBody);
            }

            String responseBody = response.body() != null ? response.body().string() : "";
            JSONObject jsonResponse = new JSONObject(responseBody);
            String messageContent = jsonResponse.getJSONArray("choices")
                    .getJSONObject(0)
                    .getJSONObject("message")
                    .getString("content");

            JSONObject json = new JSONObject(messageContent);
            Map<String, String> result = new LinkedHashMap<>();
            for (String key : json.keySet()) {
                result.put(key, json.optString(key, ""));
            }
            return result;

        } catch (Exception e) {
            e.printStackTrace();
            return Map.of("error", e.getMessage());
        }
    }
}
