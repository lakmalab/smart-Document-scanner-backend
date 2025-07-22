package edu.icet.ecom.controller;

import edu.icet.ecom.model.dto.DocumentDTO;
import edu.icet.ecom.model.dto.OCRResultDTO;
import edu.icet.ecom.model.dto.OcrSubmissionDTO;
import edu.icet.ecom.service.OCRService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ocr")
@RequiredArgsConstructor
@CrossOrigin
public class OCRController {

    private final OCRService ocrService;
    @PostMapping("/submit")
    public ResponseEntity<DocumentDTO> submitOcrAndCreateDocument(@RequestBody OcrSubmissionDTO dto) {
        DocumentDTO created = ocrService.createDocumentFromOCR(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PostMapping("/submitRaw")
    public ResponseEntity<OCRResultDTO> submitOCR(
            @RequestParam(name = "documentId") Long documentId,
            @RequestBody String rawText
    ) {
        OCRResultDTO result = ocrService.processOCRText(documentId, rawText);
        return ResponseEntity.ok(result);
    }
}
