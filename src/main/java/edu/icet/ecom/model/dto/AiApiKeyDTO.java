package edu.icet.ecom.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AiApiKeyDTO {
    private Long apiKeyID;
    private String apiKey;
    private Long userId;
    private String model;
    private LocalDateTime expiresAt;
}




