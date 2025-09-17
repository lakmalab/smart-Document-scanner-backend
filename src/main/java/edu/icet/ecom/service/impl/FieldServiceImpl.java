package edu.icet.ecom.service.impl;

import edu.icet.ecom.model.dto.FieldDTO;
import edu.icet.ecom.model.entity.FieldEntity;
import edu.icet.ecom.model.entity.TemplateEntity;
import edu.icet.ecom.repository.FieldRepository;
import edu.icet.ecom.repository.TemplateRepository;
import edu.icet.ecom.service.FieldService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FieldServiceImpl implements FieldService {
    private final FieldRepository fieldRepository;
    private final TemplateRepository templateRepository;
    private final ModelMapper modelMapper;

    @Override
    public FieldDTO createField(FieldDTO dto) {
        FieldEntity field = modelMapper.map(dto, FieldEntity.class);

        TemplateEntity template = templateRepository.findById(dto.getTemplateId())
                .orElseThrow(() -> new RuntimeException("Template not found"));

        field.setTemplate(template);
        field = fieldRepository.save(field);

        return modelMapper.map(field, FieldDTO.class);
    }
    @Override
    public List<FieldDTO> getFieldsByTemplateId(Long templateId) {
        return fieldRepository.findByTemplate_TemplateId(templateId)
                .stream()
                .map(field -> modelMapper.map(field, FieldDTO.class))
                .collect(Collectors.toList());
    }
}
