package model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExportLogDTO {
    private Long exportId;
    private String exportType;
    private LocalDateTime exportDate;
    private String filePath;
    private Long userId;
    private Long documentId;
}




