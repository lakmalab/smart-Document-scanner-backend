package edu.icet.ecom.service;

import edu.icet.ecom.model.dto.DocumentDTO;
import edu.icet.ecom.model.entity.DocumentEntity;
import edu.icet.ecom.repository.DocumentRepository;
import edu.icet.ecom.repository.TemplateRepository;
import edu.icet.ecom.repository.UserRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class DocumentService {
    private final DocumentRepository documentRepository;
    private final UserRepository userRepository;
    private final TemplateRepository templateRepository;

    public DocumentService(DocumentRepository documentRepository,
                           UserRepository userRepository,
                           TemplateRepository templateRepository) {
        this.documentRepository = documentRepository;
        this.userRepository = userRepository;
        this.templateRepository = templateRepository;
    }


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


    public List<DocumentDTO> getDocumentsByUser(Long userId) {
        return documentRepository.findByUploadedBy_UserId(userId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }


    public DocumentDTO getDocument(Long docId) {
        return documentRepository.findById(docId)
                .map(this::toDTO).orElse(null);
    }

    private DocumentDTO toDTO(DocumentEntity doc) {
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
