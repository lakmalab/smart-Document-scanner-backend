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
public class AiApiKeyEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long apiKeyID;

    private String apiKey;

    private String model;

    private LocalDateTime expiresAt;

    @OneToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
}


