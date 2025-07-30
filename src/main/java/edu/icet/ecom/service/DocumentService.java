package edu.icet.ecom.service;

import edu.icet.ecom.model.dto.DocumentDTO;
import edu.icet.ecom.model.entity.DocumentEntity;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public interface  DocumentService {
    DocumentDTO uploadDocument(Long userId, Long templateId) ;
    List<DocumentDTO> getDocumentsByUser(Long userId) ;
    DocumentDTO getDocument(Long docId) ;
    DocumentDTO toDTO(DocumentEntity doc) ;

    DocumentDTO updateDocument(Long id, DocumentDTO documentDTO);

    DocumentDTO deleteDoucument(Long id);
}
