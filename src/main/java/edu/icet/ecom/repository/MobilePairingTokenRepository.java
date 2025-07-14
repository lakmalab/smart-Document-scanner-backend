package edu.icet.ecom.repository;

import edu.icet.ecom.model.entity.MobilePairingTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MobilePairingTokenRepository extends JpaRepository<MobilePairingTokenEntity, String> {
    Optional<MobilePairingTokenEntity> findByUser_UserId(Long userId);
    Optional<MobilePairingTokenEntity> findByTokenAndUsedFalse(String token);
}


