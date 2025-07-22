package edu.icet.ecom.repository;

import edu.icet.ecom.model.entity.AiApiKeyEntity;
import edu.icet.ecom.model.entity.MobilePairingTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface MobilePairingTokenRepository extends JpaRepository<MobilePairingTokenEntity, String> {
    @Query("SELECT a FROM MobilePairingTokenEntity a WHERE a.user.id = :userId")
    Optional<MobilePairingTokenEntity> findByUserId(@Param("userId") Long userId);

    @Modifying
    @Transactional
    @Query("DELETE FROM MobilePairingTokenEntity a WHERE a.user.id = :userId")
    void deleteByUserId(@Param("userId") Long userId);
}


