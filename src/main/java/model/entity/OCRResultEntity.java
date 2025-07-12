package model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class OCRResultEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ocrId;

    @Lob
    private String rawText;

    private String ocrEngine;

    private LocalDateTime timestamp;

    @OneToOne
    @JoinColumn(name = "document_id")
    private DocumentEntity document;
}

