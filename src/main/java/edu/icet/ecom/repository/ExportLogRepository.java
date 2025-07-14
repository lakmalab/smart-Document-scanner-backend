package edu.icet.ecom.repository;

import edu.icet.ecom.model.entity.ExportLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExportLogRepository extends JpaRepository<ExportLogEntity, Long> {
    List<ExportLogEntity> findByUser_UserId(Long userId);
    List<ExportLogEntity> findByDocument_DocumentId(Long documentId);
}


