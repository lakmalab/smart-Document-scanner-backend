package edu.icet.ecom.model.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FieldDTO {
    private Long fieldId;
    private String fieldName;
    private String fieldType;
    private boolean required;
    private String aiPrompt;
    private Long templateId;
}



