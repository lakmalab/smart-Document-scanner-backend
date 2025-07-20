package edu.icet.ecom.service.impl;

import edu.icet.ecom.model.dto.AiApiKeyDTO;
import edu.icet.ecom.model.entity.AiApiKeyEntity;
import edu.icet.ecom.model.entity.UserEntity;
import edu.icet.ecom.repository.AiApiKeyRepository;
import edu.icet.ecom.repository.UserRepository;
import edu.icet.ecom.service.ApiKeyService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ApiKeyServiceImpl implements ApiKeyService {

    private final UserRepository userRepository;
    private final AiApiKeyRepository aiApiKeyRepository;
    ModelMapper modelMapper = new ModelMapper();

    public ApiKeyServiceImpl(UserRepository userRepository, AiApiKeyRepository aiApiKeyRepository) {
        this.userRepository = userRepository;
        this.aiApiKeyRepository = aiApiKeyRepository;
    }

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

}

