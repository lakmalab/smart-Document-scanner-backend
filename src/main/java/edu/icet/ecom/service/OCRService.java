package edu.icet.ecom.service;

import edu.icet.ecom.model.dto.OCRResultDTO;
import edu.icet.ecom.model.entity.DocumentEntity;
import edu.icet.ecom.model.entity.OCRResultEntity;
import edu.icet.ecom.repository.DocumentRepository;
import edu.icet.ecom.repository.OCRResultRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OCRService {

    private final DocumentRepository documentRepo;
    private final OCRResultRepository ocrRepo;
    ModelMapper modelMapper = new ModelMapper();

    public OCRResultDTO processOCRText(Long docId, String rawText) {
        DocumentEntity doc = documentRepo.findById(docId)
                .orElseThrow(() -> new RuntimeException("Document not found"));

        OCRResultEntity ocr = new OCRResultEntity();
        ocr.setDocument(doc);
        ocr.setRawText(rawText);
        ocr.setOcrEngine("AI/MLKit");
        ocr.setTimestamp(LocalDateTime.now());

        ocr = ocrRepo.save(ocr);
        return modelMapper.map(ocr, OCRResultDTO.class);
    }
}

