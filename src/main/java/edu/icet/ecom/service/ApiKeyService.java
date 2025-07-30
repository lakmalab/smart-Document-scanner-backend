package edu.icet.ecom.service;

import edu.icet.ecom.model.dto.AiApiKeyDTO;
import org.springframework.stereotype.Service;

@Service
public interface ApiKeyService {

    AiApiKeyDTO addApikey(Long userId, AiApiKeyDTO dto) ;

    AiApiKeyDTO getApiKeyById(Long userId) ;
    String getApiKeyStringByUserId(Long userId);
}

