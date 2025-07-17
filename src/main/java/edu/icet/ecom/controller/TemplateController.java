package edu.icet.ecom.controller;



import edu.icet.ecom.model.dto.TemplateDTO;
import edu.icet.ecom.service.TemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin
@RestController
@RequestMapping("/api/templates")
@RequiredArgsConstructor
public class TemplateController {

    @Autowired
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
            @PathVariable Long id,
            @RequestBody TemplateDTO dto) {
        TemplateDTO updated = templateService.updateTemplate(id, dto);
        return updated != null
                ? ResponseEntity.ok(updated)
                : ResponseEntity.notFound().build();
    }
    @GetMapping("/{id}")
    public ResponseEntity<TemplateDTO> getTemplate(@PathVariable Long id) {
        TemplateDTO template = templateService.getTemplateById(id);
        return template != null
                ? ResponseEntity.ok(template)
                : ResponseEntity.notFound().build();
    }
}

