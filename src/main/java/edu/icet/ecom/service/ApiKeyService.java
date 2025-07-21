package edu.icet.ecom.service;

import edu.icet.ecom.model.dto.AiApiKeyDTO;
import edu.icet.ecom.model.dto.LoginUserDTO;
import edu.icet.ecom.model.dto.RegisterUserDTO;
import edu.icet.ecom.model.dto.UserDTO;
import edu.icet.ecom.model.entity.AiApiKeyEntity;
import edu.icet.ecom.model.entity.UserEntity;
import edu.icet.ecom.repository.AiApiKeyRepository;
import edu.icet.ecom.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public interface ApiKeyService {

    AiApiKeyDTO addApikey(Long userId, AiApiKeyDTO dto) ;

    AiApiKeyDTO getApiKeyById(Long userId) ;

}

