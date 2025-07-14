package edu.icet.ecom.repository;

import edu.icet.ecom.model.entity.ExtractedFieldEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExtractedFieldRepository extends JpaRepository<ExtractedFieldEntity, Long> {
    List<ExtractedFieldEntity> findByDocument_DocumentId(Long documentId);
}


