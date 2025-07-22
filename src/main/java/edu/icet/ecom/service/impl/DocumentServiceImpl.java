package edu.icet.ecom.service.impl;

import edu.icet.ecom.model.dto.DocumentDTO;
import edu.icet.ecom.model.dto.ExtractedFieldDTO;
import edu.icet.ecom.model.entity.DocumentEntity;
import edu.icet.ecom.model.entity.ExtractedFieldEntity;
import edu.icet.ecom.repository.*;
import edu.icet.ecom.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
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

    @Override
    @Transactional
    public DocumentDTO updateDocument(Long id, DocumentDTO documentDTO) {
        DocumentEntity existingDocument = documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document not found"));

        existingDocument.setStatus("Reviewed");

        if (documentDTO.getExtractedFields() != null) {
            Map<Long, ExtractedFieldEntity> existingFieldMap = existingDocument.getExtractedFields().stream()
                    .filter(f -> f.getField() != null)
                    .collect(Collectors.toMap(f -> f.getField().getFieldId(), f -> f));

            for (ExtractedFieldDTO dtoField : documentDTO.getExtractedFields()) {
                ExtractedFieldEntity existingField = existingFieldMap.get(dtoField.getFieldId());

                if (existingField != null) {
                    existingField.setValue(dtoField.getValue());
                    existingField.setConfidenceScore(100F);
                } else {

                }
            }
        }

        DocumentEntity updated = documentRepository.save(existingDocument);
        return toDTO(updated);
    }

    @Override
    @Transactional
    public DocumentDTO deleteDoucument(Long id) {
        DocumentEntity document = documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document not found"));

        // This clears all child export logs (optional but safe)
        document.getExportLogs().clear();

        documentRepository.delete(document);
        return null;
    }



}
