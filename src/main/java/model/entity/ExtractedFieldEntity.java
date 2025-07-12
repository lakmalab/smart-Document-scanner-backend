package model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ExtractedFieldEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long extractedFieldId;

    private String value;

    private Float confidenceScore;

    @ManyToOne
    @JoinColumn(name = "document_id")
    private DocumentEntity document;

    @ManyToOne
    @JoinColumn(name = "field_id")
    private FieldEntity field;
}

