package edu.icet.ecom.repository;


import edu.icet.ecom.model.entity.OCRResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OCRResultRepository extends JpaRepository<OCRResultEntity, Long> {
    Optional<OCRResultEntity> findByDocument_DocumentId(Long documentId);
}
