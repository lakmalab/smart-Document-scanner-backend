package edu.icet.ecom.repository;

import edu.icet.ecom.model.entity.FieldEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FieldRepository extends JpaRepository<FieldEntity, Long> {
    List<FieldEntity> findByTemplate_TemplateId(Long templateId);
}


