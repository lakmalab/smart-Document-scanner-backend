package edu.icet.ecom.service;

import edu.icet.ecom.model.dto.AiApiKeyDTO;
import edu.icet.ecom.model.dto.LoginUserDTO;
import edu.icet.ecom.model.dto.RegisterUserDTO;
import edu.icet.ecom.model.dto.UserDTO;
import edu.icet.ecom.model.entity.AiApiKeyEntity;
import edu.icet.ecom.model.entity.UserEntity;
import edu.icet.ecom.repository.AiApiKeyRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import edu.icet.ecom.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public interface UserService {

    UserDTO registerUser(RegisterUserDTO dto);

    UserDTO getUserById(Long userId) ;

    UserDTO getUserByEmail(String email);

    List<UserDTO> getAllUsers() ;

    UserDTO loginUser(LoginUserDTO dto) ;
    UserDTO updateUser(Long userId, RegisterUserDTO dto);

    UserDTO addApikey(Long userId, AiApiKeyDTO dto) ;
}

