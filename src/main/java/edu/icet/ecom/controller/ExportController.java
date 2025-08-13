package edu.icet.ecom.controller;

import edu.icet.ecom.service.ExportService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/export")
@RequiredArgsConstructor
public class ExportController {

    private final ExportService exportService;

    @PostMapping("/xlsx/{documentId}")
    public ResponseEntity<Resource> exportToExcel(@PathVariable("documentId") Long documentId) {
        ByteArrayResource file = exportService.generateExcel(documentId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=document.xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(file);
    }
    @PostMapping("/template/{templateId}/xlsx")
    public ResponseEntity<Resource> exportTemplateDocumentsToExcel(
            @PathVariable("templateId") Long templateId,
            Authentication authentication) {

        String email = authentication.getName(); // comes from JWT `sub` claim
        ByteArrayResource file = exportService.exportTemplateToExcel(templateId, email);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=template-" + templateId + ".xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(file);
    }
    @PostMapping("/template/{templateId}/{type}/{docStatus}")
    public ResponseEntity<Resource> exportTemplateDocuments(
            @PathVariable("templateId")  Long templateId,
            @PathVariable("type")  String type,
            @PathVariable("docStatus")  String docStatus,
            Authentication authentication) {

        String email = authentication.getName();
        ByteArrayResource file = exportService.exportTemplate(templateId, email, type, docStatus);

        String fileName = "template-" + templateId + "-export." + type;

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(file);
    }



}
