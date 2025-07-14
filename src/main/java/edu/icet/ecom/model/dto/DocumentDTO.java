package edu.icet.ecom.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DocumentDTO {
    private Long documentId;
    private String status;
    private LocalDate uploadDate;
    private Long uploadedByUserId;
    private Long templateId;
    private String templateName;
    private List<ExtractedFieldDTO> extractedFields;
}




