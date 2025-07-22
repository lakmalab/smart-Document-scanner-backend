package edu.icet.ecom.model.entity;

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
public class FieldEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fieldId;

    private String fieldName;

    private String fieldType;

    private boolean required;

    @Column(length = 1000)
    private String aiPrompt;

    @ManyToOne
    @JoinColumn(name = "template_id")
    private TemplateEntity template;

    @OneToMany(mappedBy = "field")
    private List<ExtractedFieldEntity> extractedFields;
}

