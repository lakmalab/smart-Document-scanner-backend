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
import java.util.concurrent.TimeUnit;

@Service
public class AIServiceImpl implements AIService {

    private String apiKey;
    private String model;
    private final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)   // wait up to 60s for AI response
            .writeTimeout(30, TimeUnit.SECONDS)
            .build();;
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

            switch (model.toLowerCase()) {
                case "llama3.2": {
                    JSONObject jsonBody = new JSONObject();
                    jsonBody.put("prompt",  fullPrompt);

                    Request request = new Request.Builder()
                            .url("http://127.0.0.1:8000/generate")
                            .post(RequestBody.create(JSON, jsonBody.toString()))
                            .addHeader("x-api-key", "secretkey")
                            .addHeader("Content-Type", "application/json")
                            .build();

                    try (Response response = client.newCall(request).execute()) {
                        if (!response.isSuccessful()) {
                            String errorBody = response.body() != null ? response.body().string() : "";
                            return Map.of("error", "HTTP " + response.code() + ": " + errorBody);
                        }

                        String responseBody = response.body() != null ? response.body().string() : "";
                        JSONObject json = new JSONObject(responseBody);


                        Map<String, String> result = new LinkedHashMap<>();
                        result.put("response", json.optString("response", ""));
                        return result;
                    }
                }


                case "gpt-4.1": {
                    // GitHub Azure Inference API
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

                    try (Response response = client.newCall(request).execute()) {
                        if (!response.isSuccessful()) {
                            String errorBody = response.body() != null ? response.body().string() : "";
                            return Map.of("error", "HTTP " + response.code() + ": " + errorBody);
                        }

                        String responseBody = response.body() != null ? response.body().string() : "";
                        JSONObject jsonResponse = new JSONObject(responseBody);
                        System.out.println(jsonResponse);
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
                    }
                }

                default:
                    return Map.of("error", "Unsupported model: " + model);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return Map.of("error", e.getMessage());
        }
    }

}
