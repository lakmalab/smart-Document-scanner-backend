package edu.icet.ecom.service;

import com.azure.ai.inference.ChatCompletionsClient;
import com.azure.ai.inference.ChatCompletionsClientBuilder;
import com.azure.ai.inference.models.ChatCompletionsOptions;
import com.azure.ai.inference.models.ChatRequestMessage;
import com.azure.ai.inference.models.ChatRequestUserMessage;
import com.azure.core.credential.AzureKeyCredential;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public interface AIService{

    ChatCompletionsClient getClient() ;

    void setApiKey(String apikey) ;

     Map<String, String> extractFieldsFromText(String rawText, String prompt) ;
}
