package edu.icet.ecom.controller;

import edu.icet.ecom.model.dto.DocumentDTO;

import edu.icet.ecom.service.DocumentService;
import edu.icet.ecom.sse.DocumentSseEmitter;
import io.jsonwebtoken.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
public class DocumentController {
    private final DocumentSseEmitter sseEmitter;
    private final DocumentService documentService;

    @GetMapping(path = "/subscribe", produces = "text/event-stream")
    public SseEmitter subscribe() {
        System.out.println("subscribe");
        return sseEmitter.subscribe();
    }

    @PostMapping("/upload")
    public ResponseEntity<DocumentDTO> uploadDocument(@RequestParam Long userId,
                                                      @RequestParam Long templateId) {
        DocumentDTO document = documentService.uploadDocument(userId, templateId);
        return ResponseEntity.status(HttpStatus.CREATED).body(document);
    }

    @GetMapping("/by-user/{userId}")
    public List<DocumentDTO> getDocumentsByUser(@PathVariable("userId") Long userId) {
        List<DocumentDTO> docs = documentService.getDocumentsByUser(userId);

        try {
            // Immediately send any initial data if needed
            sseEmitter.sendEvent("documentsUpdate", docs);
        } catch (IOException | IllegalStateException e) {
            // Log short message, don't let the exception escape
            System.out.println("[SSE] Ignored exception while sending initial data: "
                    + e.getClass().getSimpleName());
        }
        return docs;
    }


    @GetMapping("/{id}")
    public ResponseEntity<DocumentDTO> getDocument(@PathVariable("id") Long id) {
        DocumentDTO doc = documentService.getDocument(id);
        return doc != null ? ResponseEntity.ok(doc) : ResponseEntity.notFound().build();
    }
    @PutMapping("/{id}")
    public ResponseEntity<DocumentDTO> updateDocument(@PathVariable("id") Long id,
                                                      @RequestBody DocumentDTO documentDTO) {
        DocumentDTO updatedDocument = documentService.updateDocument(id, documentDTO);
        return updatedDocument != null ? ResponseEntity.ok(updatedDocument) : ResponseEntity.notFound().build();
    }
    @DeleteMapping("/{id}")
    public Object deleteDocument(@PathVariable("id") Long id){
        DocumentDTO updatedDocument = documentService.deleteDoucument(id);
        return updatedDocument != null ? ((ResponseEntity<Void>) ResponseEntity.ok()).getBody() : ResponseEntity.notFound().build();
    }
}

