package model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MobilePairingTokenDTO {
    private String token;
    private Long userId;
    private LocalDateTime expiresAt;
    private boolean used;
}




