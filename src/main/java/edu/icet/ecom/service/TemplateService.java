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
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TemplateService {

    private final TemplateRepository templateRepository;
    private final UserRepository userRepository;
    private final FieldRepository fieldRepository;
    ModelMapper modelMapper = new ModelMapper();
    public TemplateDTO createTemplate(TemplateDTO dto) {
        // Map base template
        TemplateEntity template = modelMapper.map(dto, TemplateEntity.class);
        UserEntity user = userRepository.findById(dto.getCreatedByUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        template.setCreatedBy(user);
        template = templateRepository.save(template);

        // Map fields
        if (dto.getFields() != null) {
            TemplateEntity finalTemplate = template;
            List<FieldEntity> fields = dto.getFields().stream().map(fieldDTO -> {
                FieldEntity field = modelMapper.map(fieldDTO, FieldEntity.class);
                field.setTemplate(finalTemplate);
                return field;
            }).collect(Collectors.toList());
            fieldRepository.saveAll(fields);
            template.setFields(fields);
        }

        return modelMapper.map(template, TemplateDTO.class);
    }

    public List<TemplateDTO> getTemplatesByUserId(Long userId) {
        return templateRepository.findByCreatedBy_UserId(userId).stream()
                .map(template -> modelMapper.map(template, TemplateDTO.class))
                .collect(Collectors.toList());
    }

    public TemplateDTO getTemplateById(Long templateId) {
        return templateRepository.findById(templateId)
                .map(template -> modelMapper.map(template, TemplateDTO.class))
                .orElse(null);
    }
}

