package edu.icet.ecom.service;

import edu.icet.ecom.model.dto.TemplateDTO;
import edu.icet.ecom.model.entity.FieldEntity;
import edu.icet.ecom.model.entity.TemplateEntity;
import edu.icet.ecom.model.entity.UserEntity;
import edu.icet.ecom.repository.FieldRepository;
import edu.icet.ecom.repository.TemplateRepository;
import edu.icet.ecom.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public interface TemplateService {

     TemplateDTO createTemplate(TemplateDTO dto) ;

     List<TemplateDTO> getTemplatesByUserId(Long userId) ;

     TemplateDTO getTemplateById(Long templateId) ;

     TemplateDTO updateTemplate(Long id, TemplateDTO dto) ;

}

