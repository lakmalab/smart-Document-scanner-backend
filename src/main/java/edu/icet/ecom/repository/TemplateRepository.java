package edu.icet.ecom.repository;

import edu.icet.ecom.model.entity.TemplateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TemplateRepository extends JpaRepository<TemplateEntity, Long> {
    List<TemplateEntity> findByCreatedBy_UserId(Long userId);
}
