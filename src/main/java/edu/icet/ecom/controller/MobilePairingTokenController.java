package edu.icet.ecom.controller;

import edu.icet.ecom.model.dto.AiApiKeyDTO;
import edu.icet.ecom.model.dto.MobilePairingTokenDTO;
import edu.icet.ecom.model.dto.UserDTO;
import edu.icet.ecom.service.ApiKeyService;
import edu.icet.ecom.service.MobilePairingTokenService;
import edu.icet.ecom.util.QRCodeGeneratorUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.InetAddress;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/mobile")
@RequiredArgsConstructor
public class MobilePairingTokenController {

    private final MobilePairingTokenService service;

    @GetMapping("/users/{id}/qr")
    public ResponseEntity<String> getMobilePairingQRCode(@PathVariable("id") Long id, HttpServletRequest request) {
        MobilePairingTokenDTO tokenDTO = service.getMobilePairingToken(id);
        if (tokenDTO == null || tokenDTO.getToken() == null) {
            return ResponseEntity.notFound().build();
        }
        try {
            //  Get IP address of the machine running Spring Boot
            String localIp = InetAddress.getLocalHost().getHostAddress();

            // Optional: get port from request if needed (usually 8080 in dev)
            int port = request.getLocalPort();

            // Construct full callback URL for QR code
            String qrUrl = "http://" + localIp + ":" + port + "?token=" + tokenDTO.getToken();

            String qrBase64 = QRCodeGeneratorUtil.generateQRCodeBase64(qrUrl, 200, 200);
            return ResponseEntity.ok("data:image/png;base64," + qrBase64);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error generating QR code");
        }
    }

    @PostMapping("/users/{id}/refresh")
    public ResponseEntity<MobilePairingTokenDTO> refreshToken(@PathVariable("id") Long id) {
        MobilePairingTokenDTO refreshed = service.refreshMobilePairingToken(id);
        return refreshed != null ? ResponseEntity.ok(refreshed) : ResponseEntity.notFound().build();
    }


    @PostMapping("/add-mobile/{id}")
    public ResponseEntity<MobilePairingTokenDTO> addMobilePairingToken(
            @PathVariable("id") Long id,
            @RequestBody MobilePairingTokenDTO dto) {
        MobilePairingTokenDTO savedKey = service.addMobilePairingToken(id, dto);
        return savedKey != null
                ? ResponseEntity.ok(savedKey)
                : ResponseEntity.notFound().build();
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<MobilePairingTokenDTO> getMobilePairingToken(
            @PathVariable("id") Long id) {
        MobilePairingTokenDTO key = service.getMobilePairingToken(id);
        return key != null ? ResponseEntity.ok(key) : ResponseEntity.notFound().build();
    }
    @PostMapping("/confirm")
    public ResponseEntity<?> confirmPairing(@RequestBody Map<String, String> body) {
        String token = body.get("token");
        if (token == null || token.isBlank()) {
            return ResponseEntity.badRequest().body("Missing token");
        }

        UserDTO user = service.confirmPairing(token);

        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }


}

