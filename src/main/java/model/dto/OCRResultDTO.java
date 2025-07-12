package model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OCRResultDTO {
    private Long ocrId;
    private String rawText;
    private String ocrEngine;
    private LocalDateTime timestamp;
    private Long documentId;
}





