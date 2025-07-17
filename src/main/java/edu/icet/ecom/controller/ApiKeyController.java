package edu.icet.ecom.controller;

import edu.icet.ecom.model.dto.AiApiKeyDTO;
import edu.icet.ecom.model.dto.LoginUserDTO;
import edu.icet.ecom.model.dto.RegisterUserDTO;
import edu.icet.ecom.model.dto.UserDTO;
import edu.icet.ecom.service.ApiKeyService;
import edu.icet.ecom.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiKeyController {
    @Autowired
    private final ApiKeyService service;

    @PostMapping("/add-apikey/{id}")
    public ResponseEntity<AiApiKeyDTO> addApikey(
            @PathVariable("id") Long id,
            @RequestBody AiApiKeyDTO dto) {
        AiApiKeyDTO savedKey = service.addApikey(id, dto);
        return savedKey != null
                ? ResponseEntity.ok(savedKey)
                : ResponseEntity.notFound().build();
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<AiApiKeyDTO> getApiKey(
            @PathVariable("id") Long id) {
        AiApiKeyDTO key = service.getApiKeyById(id);
        return key != null ? ResponseEntity.ok(key) : ResponseEntity.notFound().build();
    }


}

