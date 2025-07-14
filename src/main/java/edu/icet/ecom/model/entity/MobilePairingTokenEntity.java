package edu.icet.ecom.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class MobilePairingTokenEntity {
    @Id
    private String token;

    private LocalDateTime expiresAt;

    private boolean used;

    @OneToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
}


