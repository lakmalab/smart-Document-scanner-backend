package edu.icet.ecom.service.impl;

import edu.icet.ecom.model.dto.JwtResponse;
import edu.icet.ecom.model.dto.MobilePairingTokenDTO;
import edu.icet.ecom.model.dto.UserDTO;
import edu.icet.ecom.model.entity.MobilePairingTokenEntity;
import edu.icet.ecom.model.entity.UserEntity;
import edu.icet.ecom.repository.MobilePairingTokenRepository;
import edu.icet.ecom.repository.UserRepository;
import edu.icet.ecom.service.JwtService;
import edu.icet.ecom.service.MobilePairingTokenService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MobilePairingTokenServiceImpl implements MobilePairingTokenService {
    private final UserRepository userRepository;
    private final MobilePairingTokenRepository mobilePairingTokenRepository;
    ModelMapper modelMapper = new ModelMapper();
    private final JwtService jwtService;


    @Override
    public MobilePairingTokenDTO addMobilePairingToken(Long userId, MobilePairingTokenDTO dto) {
        Optional<UserEntity> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) return null;

        UserEntity user = optionalUser.get();

        MobilePairingTokenEntity mobilePairingTokenEntity = new MobilePairingTokenEntity();
        mobilePairingTokenEntity.setToken(dto.getToken());
        mobilePairingTokenEntity.setExpiresAt(dto.getExpiresAt());
        mobilePairingTokenEntity.setUser(user);

        MobilePairingTokenEntity saved = mobilePairingTokenRepository.save(mobilePairingTokenEntity);

        return modelMapper.map(saved, MobilePairingTokenDTO.class);
    }
    @Override
    public MobilePairingTokenDTO getMobilePairingToken(Long userId) {
        return mobilePairingTokenRepository.findByUserId(userId)
                .map(key -> modelMapper.map(key, MobilePairingTokenDTO.class))
                .orElse(null);
    }

    @Override
    @Transactional
    public MobilePairingTokenDTO refreshMobilePairingToken(Long userId) {
        Optional<UserEntity> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) return null;

        // Force deletion before insertion
        mobilePairingTokenRepository.deleteByUserId(userId);

        MobilePairingTokenEntity entity = new MobilePairingTokenEntity();
        entity.setToken(UUID.randomUUID().toString());
        entity.setExpiresAt(LocalDate.now().plusDays(7).atStartOfDay());
        entity.setUsed(false);

        // Set the user reference
        UserEntity user = optionalUser.get();
        entity.setUser(user); // Assuming you have a setUser method in MobilePairingTokenEntity

        MobilePairingTokenEntity saved = mobilePairingTokenRepository.save(entity);
        return modelMapper.map(saved, MobilePairingTokenDTO.class);
    }

    @Override
    public JwtResponse confirmPairing(String token) {
        MobilePairingTokenEntity entity = mobilePairingTokenRepository.findById(token).orElse(null);

        if (entity == null || entity.isUsed() || entity.getExpiresAt().isBefore(LocalDateTime.now())) {
            return null;
        }

        // Mark token as used
        entity.setUsed(true);
        mobilePairingTokenRepository.save(entity);

        UserEntity user = entity.getUser();

        String role = user.getRole(); // Example: "ROLE_USER" or "ADMIN"

        // Generate JWT
        String jwt = jwtService.generateToken(user.getEmail(), role,"MOBILE"); // user must implement UserDetails or be wrapped
        System.out.println(jwt);
        UserDTO dto = modelMapper.map(user, UserDTO.class);
        return new JwtResponse(jwt, dto);
    }



}

