package edu.icet.ecom.service;

import edu.icet.ecom.model.dto.AiApiKeyDTO;
import edu.icet.ecom.model.dto.LoginUserDTO;
import edu.icet.ecom.model.dto.RegisterUserDTO;
import edu.icet.ecom.model.dto.UserDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public interface UserService {

    UserDTO registerUser(RegisterUserDTO dto);

    UserDTO getUserById(Long userId) ;

    UserDTO getUserByEmail(String email);

    List<UserDTO> getAllUsers() ;

    UserDTO loginUser(LoginUserDTO dto) ;
    UserDTO updateUser(Long userId, UserDTO dto);
    UserDTO updateUser(Long userId, RegisterUserDTO dto);
    UserDTO addApikey(Long userId, AiApiKeyDTO dto) ;


    UserDTO updateProfilePictureUrl(Long id, String imageUrl);
}

