package edu.icet.ecom.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TemplateDTO {
    private Long templateId;
    private String templateName;
    private String documentType;
    private Long createdByUserId;
    private List<FieldDTO> fields;
}


