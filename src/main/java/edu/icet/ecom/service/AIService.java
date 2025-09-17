package edu.icet.ecom.service;

import com.azure.ai.inference.ChatCompletionsClient;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public interface AIService{

    ChatCompletionsClient getClient() ;

    void setApiKey(String apikey,String model) ;

    Map<String, String> extractFieldsFromText(String rawText, String prompt) ;
}
