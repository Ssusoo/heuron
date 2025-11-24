package com.me.heuron.domains.patient.domain;

import com.me.heuron.base.domain.BaseDateEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

@Table(name = "HEURON_IMG")
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class PatientImage extends BaseDateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PAT_IMG_SERLNO", nullable = false, updatable = false)
    private Long patientImageSerialNo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PAT_SERLNO", nullable = false, updatable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Patient patient; // 환자

    @Column(name = "PAT_IMG_TYPE", length = 100)
    private String patientImageType; // 파일 유형

    @Column(name = "PAT_IMG_PATHWY", nullable = false, updatable = false)
    private String patientImagePath; // 파일 경로

    @Column(name = "PAT_IMG_NM", length = 100, nullable = false, updatable = false)
    private String patientImageName; // 파일 명

    @Column(name = "PAT_ORG_IMG_NM", length = 100)
    private String patientOriginalImageName; // 원본 파일 명

    @Column(name = "PAT_IMG_EXTNSN", length = 10)
    private String patientImageExtension; // 파일 확장자

    public static PatientImage create(Patient patient, Path patientPath, String storedFileName, String originalName, String extension, MultipartFile file) {
        return PatientImage.builder()
                .patient(patient)
                .patientImageType(file.getContentType())
                .patientImagePath(patientPath.toString())
                .patientImageName(storedFileName)
                .patientOriginalImageName(originalName)
                .patientImageExtension(extension)
                .build();
    }
}
