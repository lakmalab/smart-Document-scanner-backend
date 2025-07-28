package edu.icet.ecom.service;

import edu.icet.ecom.model.dto.DocumentDTO;
import edu.icet.ecom.model.dto.OCRResultDTO;
import edu.icet.ecom.model.dto.OcrSubmissionDTO;
import org.springframework.stereotype.Service;


@Service
public interface OCRService {


    OCRResultDTO processOCRText(Long docId, String rawText);
    DocumentDTO createDocumentFromOCR(OcrSubmissionDTO dto) ;
}

