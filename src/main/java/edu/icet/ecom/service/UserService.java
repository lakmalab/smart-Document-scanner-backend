package edu.icet.ecom.service;

import edu.icet.ecom.model.dto.LoginUserDTO;
import edu.icet.ecom.model.dto.RegisterUserDTO;
import edu.icet.ecom.model.dto.UserDTO;
import edu.icet.ecom.model.entity.UserEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import edu.icet.ecom.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    ModelMapper modelMapper = new ModelMapper();

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDTO registerUser(RegisterUserDTO dto) {
        UserEntity user = modelMapper.map(dto, UserEntity.class);
        user.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        return modelMapper.map(userRepository.save(user), UserDTO.class);
    }

    public UserDTO getUserById(Long userId) {
        return userRepository.findById(userId)
                .map(user -> modelMapper.map(user, UserDTO.class))
                .orElse(null);
    }

    public UserDTO getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(user -> modelMapper.map(user, UserDTO.class))
                .orElse(null);
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .collect(Collectors.toList());
    }

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



}

