package model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FieldDTO {
    private Long fieldId;
    private String fieldName;
    private String fieldType;
    private boolean required;
}



