package edu.icet.ecom.service.impl;

import edu.icet.ecom.model.dto.DocumentDTO;
import edu.icet.ecom.model.dto.OCRResultDTO;
import edu.icet.ecom.model.dto.OcrSubmissionDTO;
import edu.icet.ecom.model.entity.*;
import edu.icet.ecom.repository.*;
import edu.icet.ecom.service.AIService;
import edu.icet.ecom.service.ApiKeyService;
import edu.icet.ecom.service.OCRService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    private final ModelMapper modelMapper;

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


        DocumentEntity document = new DocumentEntity();
        document.setUploadedBy(user);
        document.setTemplate(template);
        document.setUploadDate(LocalDate.now());
        document.setStatus("Pending");
        documentRepo.save(document);


        OCRResultEntity ocr = new OCRResultEntity();
        ocr.setDocument(document);
        ocr.setRawText(dto.getRawText());
        ocr.setOcrEngine("MLKit");
        ocr.setTimestamp(LocalDateTime.now());
        ocrRepo.save(ocr);


        String apikey = apiKeyService.getApiKeyStringByUserId(dto.getUserId());
        String model = apiKeyService.getAiModeByUserId(dto.getUserId());
        aiService.setApiKey(apikey,model);


        StringBuilder jsonKeys = new StringBuilder();
        StringBuilder guidelines = new StringBuilder();

        jsonKeys.append("{\n");
        for (FieldEntity field : template.getFields()) {
            String name = field.getFieldName();
            jsonKeys.append("  \"").append(name).append("\": \"\",\n");

            if (field.getAiPrompt() != null && !field.getAiPrompt().isBlank()) {
                guidelines.append("- ").append(name).append(": ").append(field.getAiPrompt()).append("\n");
            }
        }

        if (jsonKeys.charAt(jsonKeys.length() - 2) == ',') {
            jsonKeys.setLength(jsonKeys.length() - 2);
            jsonKeys.append("\n");
        }
        jsonKeys.append("}");

        String fullPrompt = """
        You are an intelligent document parser. Extract the following fields from the provided text.
        Do NOT rely on exact labels — infer based on the meaning and context.

        Return the result as pure JSON like this (and nothing else):

        %s

        Guidelines:
        %s

        If any field is missing, return it as an empty string.
        """.formatted(jsonKeys.toString(), guidelines.toString());

        Map<String, String> extractedMap = aiService.extractFieldsFromText(dto.getRawText(), fullPrompt);

        if (extractedMap.containsKey("error")) {
            System.out.println("AI extraction failed: {}"+ extractedMap.get("error"));
            return null;
        }
        List<ExtractedFieldEntity> extractedFields = new ArrayList<>();


        for (FieldEntity field : template.getFields()) {
            String value = extractedMap.getOrDefault(field.getFieldName(), "");
            ExtractedFieldEntity extracted = new ExtractedFieldEntity();
            extracted.setDocument(document);
            extracted.setField(field);
            extracted.setValue(value);
            extracted.setConfidenceScore(0.95f);
            extractedFields.add(extracted);
        }

        extractedFieldRepo.saveAll(extractedFields);

        return modelMapper.map(document, DocumentDTO.class);
    }

}

