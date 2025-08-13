package edu.icet.ecom.service;

import edu.icet.ecom.model.dto.TemplateDTO;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public interface TemplateService {

     TemplateDTO createTemplate(TemplateDTO dto) ;

     List<TemplateDTO> getTemplatesByUserId(Long userId) ;

     TemplateDTO getTemplateById(Long templateId) ;

     TemplateDTO updateTemplate(Long id, TemplateDTO dto) ;

     TemplateDTO updateTemplateImagePath(Long id, String imageUrl);

     boolean deleteTemplate(Long id);
}

