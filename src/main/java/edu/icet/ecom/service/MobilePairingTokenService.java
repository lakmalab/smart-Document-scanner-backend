package edu.icet.ecom.service;

import edu.icet.ecom.model.dto.MobilePairingTokenDTO;
import edu.icet.ecom.model.dto.UserDTO;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public interface MobilePairingTokenService {
    MobilePairingTokenDTO addMobilePairingToken(Long userId, MobilePairingTokenDTO dto);
    MobilePairingTokenDTO getMobilePairingToken(Long userId) ;
    @Transactional
    MobilePairingTokenDTO refreshMobilePairingToken(Long userId);
    UserDTO confirmPairing(String token);
}

