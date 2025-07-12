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
public class TemplateEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long templateId;

    private String templateName;

    private String documentType;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private UserEntity createdBy;

    @OneToMany(mappedBy = "template", cascade = CascadeType.ALL)
    private List<FieldEntity> fields;

    @OneToMany(mappedBy = "template")
    private List<DocumentEntity> documents;
}

