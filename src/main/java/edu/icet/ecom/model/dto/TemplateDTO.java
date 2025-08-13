package edu.icet.ecom.model.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TemplateDTO {
    private Long templateId;
    private String templateName;
    private String templateImagePath;
    private String documentType;
    private Long createdByUserId;
    private List<FieldDTO> fields;
}


