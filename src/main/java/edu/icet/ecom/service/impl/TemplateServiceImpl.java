package edu.icet.ecom.service.impl;

import edu.icet.ecom.model.dto.TemplateDTO;
import edu.icet.ecom.model.entity.FieldEntity;
import edu.icet.ecom.model.entity.TemplateEntity;
import edu.icet.ecom.model.entity.UserEntity;
import edu.icet.ecom.repository.FieldRepository;
import edu.icet.ecom.repository.TemplateRepository;
import edu.icet.ecom.repository.UserRepository;
import edu.icet.ecom.service.TemplateService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TemplateServiceImpl implements TemplateService {

    private final TemplateRepository templateRepository;
    private final UserRepository userRepository;
    private final FieldRepository fieldRepository;
    ModelMapper modelMapper = new ModelMapper();

    public TemplateDTO createTemplate(TemplateDTO dto) {
        TemplateEntity template = new TemplateEntity();
        template.setTemplateName(dto.getTemplateName());
        template.setDocumentType(dto.getDocumentType());

        UserEntity user = userRepository.findById(dto.getCreatedByUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        template.setCreatedBy(user);

        template = templateRepository.save(template);

        if (dto.getFields() != null && !dto.getFields().isEmpty()) {
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

    @Override
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
    @Override
    public TemplateDTO updateTemplate(Long id, TemplateDTO dto) {
        TemplateEntity existingTemplate = templateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Template not found"));

        existingTemplate.setTemplateName(dto.getTemplateName());

        fieldRepository.deleteAll(existingTemplate.getFields());

        if (dto.getFields() != null) {
            List<FieldEntity> updatedFields = dto.getFields().stream().map(fieldDTO -> {
                FieldEntity field = modelMapper.map(fieldDTO, FieldEntity.class);
                field.setTemplate(existingTemplate);
                return field;
            }).collect(Collectors.toList());

            existingTemplate.setFields(updatedFields);
            fieldRepository.saveAll(updatedFields);
        }

        TemplateEntity updatedTemplate = templateRepository.save(existingTemplate);

        return modelMapper.map(updatedTemplate, TemplateDTO.class);
    }

}

