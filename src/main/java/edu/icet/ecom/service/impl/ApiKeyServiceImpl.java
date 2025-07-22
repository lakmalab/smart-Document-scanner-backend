package edu.icet.ecom.service.impl;

import edu.icet.ecom.model.dto.AiApiKeyDTO;
import edu.icet.ecom.model.entity.AiApiKeyEntity;
import edu.icet.ecom.model.entity.UserEntity;
import edu.icet.ecom.repository.AiApiKeyRepository;
import edu.icet.ecom.repository.UserRepository;
import edu.icet.ecom.service.ApiKeyService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ApiKeyServiceImpl implements ApiKeyService {

    private final UserRepository userRepository;
    private final AiApiKeyRepository aiApiKeyRepository;
    ModelMapper modelMapper = new ModelMapper();


    @Override
    public AiApiKeyDTO addApikey(Long userId, AiApiKeyDTO dto) {
        Optional<UserEntity> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) return null;

        UserEntity user = optionalUser.get();

        AiApiKeyEntity apiKeyEntity = new AiApiKeyEntity();
        apiKeyEntity.setApiKey(dto.getApiKey());
        apiKeyEntity.setModel(dto.getModel());
        apiKeyEntity.setExpiresAt(dto.getExpiresAt());
        apiKeyEntity.setUser(user);

        AiApiKeyEntity saved = aiApiKeyRepository.save(apiKeyEntity);

        return modelMapper.map(saved, AiApiKeyDTO.class);
    }
    @Override
    public AiApiKeyDTO getApiKeyById(Long userId) {
        return aiApiKeyRepository.findByUserId(userId)
                .map(key -> modelMapper.map(key, AiApiKeyDTO.class))
                .orElse(null);
    }
    @Override
    public String getApiKeyStringByUserId(Long userId) {
        return aiApiKeyRepository.findByUserId(userId)
                .map(AiApiKeyEntity::getApiKey)
                .orElse(null);
    }
}

