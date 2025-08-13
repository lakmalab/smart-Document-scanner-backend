package edu.icet.ecom.controller;



import edu.icet.ecom.model.dto.TemplateDTO;
import edu.icet.ecom.model.dto.UserDTO;
import edu.icet.ecom.service.TemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/api/templates")
@RequiredArgsConstructor
public class TemplateController {

    private final TemplateService templateService;

    @PostMapping
    public ResponseEntity<TemplateDTO> createTemplate(@RequestBody TemplateDTO dto) {
        TemplateDTO created = templateService.createTemplate(dto);
        return ResponseEntity.status(201).body(created);
    }

    @GetMapping("/by-user/{userId}")
    public List<TemplateDTO> getTemplatesByUser(@PathVariable("userId") Long userId) {
        return templateService.getTemplatesByUserId(userId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TemplateDTO> updateTemplate(
            @PathVariable("id") Long id,
            @RequestBody TemplateDTO dto) {
        System.out.println(dto);
        TemplateDTO updated = templateService.updateTemplate(id, dto);
        return updated != null
                ? ResponseEntity.ok(updated)
                : ResponseEntity.notFound().build();
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTemplate(
            @PathVariable("id") Long id) {
        boolean deleted = templateService.deleteTemplate(id);
        return deleted != false
                ? ResponseEntity.ok(deleted)
                : ResponseEntity.notFound().build();
    }
    @PostMapping("/{id}/template-image")
    public ResponseEntity<TemplateDTO> updateTemplateImagePath(
            @PathVariable("id")  Long id,
            @RequestBody Map<String, String> requestBody) {  // Changed to @RequestBody

        String imageUrl = requestBody.get("url");
        if (imageUrl == null || imageUrl.isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        TemplateDTO updated = templateService.updateTemplateImagePath(id, imageUrl);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TemplateDTO> getTemplate(@PathVariable("id") Long id) {
        TemplateDTO template = templateService.getTemplateById(id);
        return template != null
                ? ResponseEntity.ok(template)
                : ResponseEntity.notFound().build();
    }
}

