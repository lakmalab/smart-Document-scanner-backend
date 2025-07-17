package edu.icet.ecom.repository;

import edu.icet.ecom.model.entity.AiApiKeyEntity;
import edu.icet.ecom.model.entity.AuditLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AiApiKeyRepository extends JpaRepository<AiApiKeyEntity, String> {
    @Query("SELECT a FROM AiApiKeyEntity a WHERE a.user.id = :userId")
    Optional<AiApiKeyEntity> findByUserId(@Param("userId") Long userId);
}


