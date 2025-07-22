package edu.icet.ecom.service;

import edu.icet.ecom.model.dto.DocumentDTO;
import edu.icet.ecom.model.dto.ExtractedFieldDTO;
import edu.icet.ecom.model.entity.DocumentEntity;
import edu.icet.ecom.model.entity.ExtractedFieldEntity;
import edu.icet.ecom.repository.DocumentRepository;
import edu.icet.ecom.repository.ExtractedFieldRepository;
import edu.icet.ecom.repository.TemplateRepository;
import edu.icet.ecom.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
@Service
public interface  DocumentService {
    DocumentDTO uploadDocument(Long userId, Long templateId) ;
    List<DocumentDTO> getDocumentsByUser(Long userId) ;
    DocumentDTO getDocument(Long docId) ;
    DocumentDTO toDTO(DocumentEntity doc) ;

    DocumentDTO updateDocument(Long id, DocumentDTO documentDTO);

    DocumentDTO deleteDoucument(Long id);
}
