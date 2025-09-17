package edu.icet.ecom.service.impl;

import edu.icet.ecom.model.dto.AiApiKeyDTO;
import edu.icet.ecom.model.dto.LoginUserDTO;
import edu.icet.ecom.model.dto.RegisterUserDTO;
import edu.icet.ecom.model.dto.UserDTO;
import edu.icet.ecom.model.entity.AiApiKeyEntity;
import edu.icet.ecom.model.entity.UserEntity;
import edu.icet.ecom.repository.AiApiKeyRepository;
import edu.icet.ecom.repository.UserRepository;
import edu.icet.ecom.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AiApiKeyRepository aiApiKeyRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @Override
    public UserDTO registerUser(RegisterUserDTO dto) {
        UserEntity user = modelMapper.map(dto, UserEntity.class);
        user.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        return modelMapper.map(userRepository.save(user), UserDTO.class);
    }
    @Override
    public UserDTO getUserById(Long userId) {
        return userRepository.findById(userId)
                .map(user -> modelMapper.map(user, UserDTO.class))
                .orElse(null);
    }
    @Override
    public UserDTO getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(user -> modelMapper.map(user, UserDTO.class))
                .orElse(null);
    }
    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .collect(Collectors.toList());
    }
    @Override
    public UserDTO loginUser(LoginUserDTO dto) {
        Optional<UserEntity> userOpt = userRepository.findByEmail(dto.getEmail());
        if (userOpt.isPresent()) {
            UserEntity user = userOpt.get();
            if (passwordEncoder.matches(dto.getPassword(), user.getPasswordHash())) {
                return modelMapper.map(user, UserDTO.class);
            }
        }
        return null;
    }
    @Override
    public UserDTO updateUser(Long userId, RegisterUserDTO dto) {
        Optional<UserEntity> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) return null;

        UserEntity user = optionalUser.get();

        user.setName(dto.getName());
        user.setEmail(dto.getEmail());

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            user.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        }

        return modelMapper.map(userRepository.save(user), UserDTO.class);
    }
    @Override
    public UserDTO updateUser(Long userId, UserDTO dto) {
        Optional<UserEntity> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) return null;

        UserEntity user = optionalUser.get();

        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setAddress(dto.getAddress());
        user.setCity(dto.getCity());
        user.setContactNumber(dto.getContactNumber());
        user.setProvince(dto.getProvince());
        user.setProfilePicturePath(dto.getProfilePicturePath());
        return modelMapper.map(userRepository.save(user), UserDTO.class);
    }
    @Override
    public UserDTO addApikey(Long userId, AiApiKeyDTO dto) {
        Optional<UserEntity> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) return null;

        UserEntity user = optionalUser.get();

        // Create or update API key
        AiApiKeyEntity apiKeyEntity = new AiApiKeyEntity();
        apiKeyEntity.setApiKey(dto.getApiKey());
        apiKeyEntity.setModel(dto.getModel());
        apiKeyEntity.setExpiresAt(dto.getExpiresAt());
        apiKeyEntity.setUser(user);

        // Save the API key
        aiApiKeyRepository.save(apiKeyEntity);

        userRepository.save(user);

        return modelMapper.map(user, UserDTO.class);
    }

    @Override
    public UserDTO updateProfilePictureUrl(Long userId, String imageUrl) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.setProfilePicturePath(imageUrl);
        UserEntity savedUser = userRepository.save(user);
        return modelMapper.map(savedUser, UserDTO.class);
    }

}

