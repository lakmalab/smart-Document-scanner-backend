package edu.icet.ecom.service.impl;

import edu.icet.ecom.model.dto.DocumentDTO;
import edu.icet.ecom.model.dto.ExtractedFieldDTO;
import edu.icet.ecom.model.entity.DocumentEntity;
import edu.icet.ecom.model.entity.ExtractedFieldEntity;
import edu.icet.ecom.repository.DocumentRepository;
import edu.icet.ecom.repository.ExtractedFieldRepository;
import edu.icet.ecom.repository.TemplateRepository;
import edu.icet.ecom.repository.UserRepository;
import edu.icet.ecom.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;
    private final UserRepository userRepository;
    private final TemplateRepository templateRepository;
    ModelMapper modelMapper = new ModelMapper();
    private final ExtractedFieldRepository extractedFieldRepository;



    @Override
    public DocumentDTO uploadDocument(Long userId, Long templateId) {
        DocumentEntity doc = new DocumentEntity();
        doc.setUploadDate(LocalDate.now());
        doc.setStatus("Pending");

        doc.setUploadedBy(userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found")));

        doc.setTemplate(templateRepository.findById(templateId)
                .orElseThrow(() -> new RuntimeException("Template not found")));

        return toDTO(documentRepository.save(doc));
    }

    @Override
    public List<DocumentDTO> getDocumentsByUser(Long userId) {
        List<DocumentEntity> docs = documentRepository.findByUploadedBy_UserId(userId);

        return docs.stream().map(doc -> {
            DocumentDTO dto = modelMapper.map(doc, DocumentDTO.class);

            List<ExtractedFieldEntity> fields = extractedFieldRepository.findByDocument_DocumentId(doc.getDocumentId());

            List<ExtractedFieldDTO> fieldDTOs = fields.stream()
                    .map(field -> modelMapper.map(field, ExtractedFieldDTO.class))
                    .collect(Collectors.toList());

            dto.setExtractedFields(fieldDTOs);

            return dto;
        }).collect(Collectors.toList());
    }


    @Override
    public DocumentDTO getDocument(Long docId) {
        DocumentEntity doc = documentRepository.findById(docId)
                .orElseThrow(() -> new RuntimeException("Document not found"));

        DocumentDTO dto = modelMapper.map(doc, DocumentDTO.class);

        // Fetch and map extracted fields manually
        List<ExtractedFieldEntity> fields = extractedFieldRepository.findByDocument_DocumentId(docId);
        List<ExtractedFieldDTO> fieldDTOs = fields.stream()
                .map(field -> modelMapper.map(field, ExtractedFieldDTO.class))
                .collect(Collectors.toList());

        dto.setExtractedFields(fieldDTOs);

        return dto;
    }

    @Override
    public DocumentDTO toDTO(DocumentEntity doc) {
        DocumentDTO dto = new DocumentDTO();
        dto.setDocumentId(doc.getDocumentId());
        dto.setStatus(doc.getStatus());
        dto.setUploadDate(doc.getUploadDate());
        dto.setTemplateId(doc.getTemplate().getTemplateId());
        dto.setTemplateName(doc.getTemplate().getTemplateName());
        dto.setUploadedByUserId(doc.getUploadedBy().getUserId());
        return dto;
    }
}
