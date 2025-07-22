package edu.icet.ecom.controller;

import edu.icet.ecom.model.dto.FieldDTO;
import edu.icet.ecom.service.FieldService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fields")
@RequiredArgsConstructor
public class FieldController {

    private final FieldService fieldService;

    @PostMapping
    public ResponseEntity<FieldDTO> createField(@RequestBody FieldDTO dto) {
        FieldDTO created = fieldService.createField(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/by-template/{templateId}")
    public List<FieldDTO> getFieldsByTemplate(@PathVariable("templateId") Long templateId) {
        return fieldService.getFieldsByTemplateId(templateId);
    }
}

