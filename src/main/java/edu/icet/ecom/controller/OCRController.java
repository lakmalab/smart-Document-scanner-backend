package edu.icet.ecom.controller;

import edu.icet.ecom.model.dto.OCRResultDTO;
import edu.icet.ecom.service.OCRService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ocr")
@RequiredArgsConstructor
@CrossOrigin
public class OCRController {

    private final OCRService ocrService;

    @PostMapping("/submit")
    public ResponseEntity<OCRResultDTO> submitOCR(
            @RequestParam(name = "documentId") Long documentId,
            @RequestBody String rawText
    ) {
        OCRResultDTO result = ocrService.processOCRText(documentId, rawText);
        return ResponseEntity.ok(result);
    }
}
