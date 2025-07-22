package edu.icet.ecom.model.entity;

import jakarta.persistence.*;
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
@Entity
public class DocumentEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long documentId;

    private LocalDate uploadDate;

    private String status;

    @ManyToOne
    @JoinColumn(name = "template_id")
    private TemplateEntity template;

    @ManyToOne
    @JoinColumn(name = "uploaded_by")
    private UserEntity uploadedBy;

    @OneToMany(mappedBy = "document", cascade = CascadeType.ALL)
    private List<ExtractedFieldEntity> extractedFields;

    @OneToOne(mappedBy = "document", cascade = CascadeType.ALL)
    private OCRResultEntity ocrResult;

    @OneToMany(mappedBy = "document")
    private List<AuditLogEntity> auditLogs;

    @OneToMany(mappedBy = "document", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExportLogEntity> exportLogs;

}
