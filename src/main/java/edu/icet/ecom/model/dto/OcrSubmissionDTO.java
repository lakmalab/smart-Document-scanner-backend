package edu.icet.ecom.model.dto;

import lombok.Data;

@Data
public class OcrSubmissionDTO {
    private Long userId;
    private Long templateId;
    private String rawText;
}
