package edu.icet.ecom.repository;

import edu.icet.ecom.model.entity.DocumentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<DocumentEntity, Long> {
    List<DocumentEntity> findByUploadedBy_UserId(Long userId);
    List<DocumentEntity> findByTemplate_TemplateId(Long templateId);
}

