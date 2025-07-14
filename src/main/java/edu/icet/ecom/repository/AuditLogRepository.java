package edu.icet.ecom.repository;

import edu.icet.ecom.model.entity.AuditLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLogEntity, Long> {
    List<AuditLogEntity> findByUser_UserId(Long userId);
    List<AuditLogEntity> findByDocument_DocumentId(Long documentId);
}

