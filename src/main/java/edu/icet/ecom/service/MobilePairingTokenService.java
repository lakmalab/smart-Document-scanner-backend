package edu.icet.ecom.service;

import edu.icet.ecom.model.dto.AiApiKeyDTO;
import edu.icet.ecom.model.dto.MobilePairingTokenDTO;
import edu.icet.ecom.model.dto.UserDTO;
import edu.icet.ecom.model.entity.AiApiKeyEntity;
import edu.icet.ecom.model.entity.MobilePairingTokenEntity;
import edu.icet.ecom.model.entity.UserEntity;
import edu.icet.ecom.repository.AiApiKeyRepository;
import edu.icet.ecom.repository.MobilePairingTokenRepository;
import edu.icet.ecom.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public interface MobilePairingTokenService {
    MobilePairingTokenDTO addMobilePairingToken(Long userId, MobilePairingTokenDTO dto);
    MobilePairingTokenDTO getMobilePairingToken(Long userId) ;
    @Transactional
    MobilePairingTokenDTO refreshMobilePairingToken(Long userId);
    UserDTO confirmPairing(String token);
}

