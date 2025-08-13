package edu.icet.ecom.model.dto;

import edu.icet.ecom.model.entity.FieldEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExtractedFieldDTO {
    private Long fieldId;
    private String fieldName;
    private String value;
    private Float confidenceScore;
}




