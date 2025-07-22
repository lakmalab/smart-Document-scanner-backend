package edu.icet.ecom.service.impl;

import edu.icet.ecom.model.dto.DocumentDTO;
import edu.icet.ecom.model.dto.OCRResultDTO;
import edu.icet.ecom.model.dto.OcrSubmissionDTO;
import edu.icet.ecom.model.entity.*;
import edu.icet.ecom.repository.*;
import edu.icet.ecom.service.ApiKeyService;
import edu.icet.ecom.service.OCRService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OCRServiceImpl implements OCRService {

    private final UserRepository userRepo;
    private final TemplateRepository templateRepo;
    private final DocumentRepository documentRepo;
    private final OCRResultRepository ocrRepo;
    private final ExtractedFieldRepository extractedFieldRepo;
    private final ApiKeyService apiKeyService;
    private final AIService aiService;


    ModelMapper modelMapper = new ModelMapper();

    @Override
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

    public DocumentDTO createDocumentFromOCR(OcrSubmissionDTO dto) {
        UserEntity user = userRepo.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        TemplateEntity template = templateRepo.findById(dto.getTemplateId())
                .orElseThrow(() -> new RuntimeException("Template not found"));

        // 1. Create Document
        DocumentEntity document = new DocumentEntity();
        document.setUploadedBy(user);
        document.setTemplate(template);
        document.setUploadDate(LocalDate.now());
        document.setStatus("Pending");
        documentRepo.save(document);

        // 2. Save OCR text
        OCRResultEntity ocr = new OCRResultEntity();
        ocr.setDocument(document);
        ocr.setRawText(dto.getRawText());
        ocr.setOcrEngine("MLKit or Custom");
        ocr.setTimestamp(LocalDateTime.now());
        ocrRepo.save(ocr);

        // 3. AI Extraction
        List<ExtractedFieldEntity> extractedFields = new ArrayList<>();

        for (FieldEntity field : template.getFields()) {
            String prompt = field.getAiPrompt() != null
                    ? field.getAiPrompt()
                    : "Extract the value for field: " + field.getFieldName();
           String apikey =  apiKeyService.getApiKeyStringByUserId(dto.getUserId());
           this.aiService.setApiKey(apikey);
            String value = aiService.extractValue(dto.getRawText(), prompt);

            ExtractedFieldEntity extracted = new ExtractedFieldEntity();
            extracted.setDocument(document);
            extracted.setField(field);
            extracted.setValue(value);
            extracted.setConfidenceScore(0.95f); // or from AI if supported
            extractedFields.add(extracted);
        }

        extractedFieldRepo.saveAll(extractedFields);

        return modelMapper.map(document, DocumentDTO.class);
    }
}

