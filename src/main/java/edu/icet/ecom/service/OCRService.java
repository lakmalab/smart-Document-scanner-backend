package edu.icet.ecom.service;

import edu.icet.ecom.model.dto.DocumentDTO;
import edu.icet.ecom.model.dto.OCRResultDTO;
import edu.icet.ecom.model.dto.OcrSubmissionDTO;
import edu.icet.ecom.model.entity.DocumentEntity;
import edu.icet.ecom.model.entity.OCRResultEntity;
import edu.icet.ecom.repository.DocumentRepository;
import edu.icet.ecom.repository.OCRResultRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public interface OCRService {


    OCRResultDTO processOCRText(Long docId, String rawText);
    DocumentDTO createDocumentFromOCR(OcrSubmissionDTO dto) ;
}

