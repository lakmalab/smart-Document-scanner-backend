package edu.icet.ecom.model.entity;

import edu.icet.ecom.enums.UserRole;
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
@Table(name = "users")
public class UserEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String profilePicturePath;

    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    private String address;

    private String contactNumber;

    private String city;

    private String province;

    @Column(nullable = false)
    private String passwordHash;

    @OneToMany(mappedBy = "createdBy")
    private List<TemplateEntity> templates;

    @OneToMany(mappedBy = "uploadedBy")
    private List<DocumentEntity> documents;

    @OneToMany(mappedBy = "user")
    private List<AuditLogEntity> auditLogs;

    @OneToMany(mappedBy = "user")
    private List<ExportLogEntity> exportLogs;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private MobilePairingTokenEntity mobilePairingToken;

    private UserRole role;
}
