package com.me.heuron.domains.patient.domain;

import com.me.heuron.base.domain.BaseDateEntity;
import com.me.heuron.domains.patient.dto.PatientCreateRequest;
import com.me.heuron.global.constant.Gender;
import com.me.heuron.global.constant.Yn;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Table(name = "HEURON_PAT")
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Patient extends BaseDateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PAT_SERLNO", nullable = false, updatable = false)
    private Long patientSerialNo;

    @OneToMany(mappedBy = "patient", fetch = FetchType.LAZY)
    private final List<PatientImage> patientImages;

    @Column(name = "PAT_NM", length = 100, nullable = false)
    private String patientName; // 이름

    @Column(name = "PAT_AGE", nullable = false)
    private int patientAge; // 나이

    @Enumerated(value = EnumType.STRING)
    @Column(name = "PAT_GNDR", nullable = false)
    private Gender patientGender; // 성별

    @Enumerated(value = EnumType.STRING)
    @Column(name = "PAT_DIS_YN", length = 1, nullable = false)
    private Yn patientDiseaseYn; // 질병 여부

    @Enumerated(value = EnumType.STRING)
    @Column(name = "DEL_YN", length = 1, nullable = false)
    private Yn deleteYn; // 삭제 여부

    public static Patient create(PatientCreateRequest request) {
        return Patient.builder()
                .patientName(request.patientName())
                .patientAge(request.patientAge())
                .patientGender(request.patientGender())
                .patientDiseaseYn(request.patientDiseaseYn())
                .deleteYn(Yn.N)
                .build();
    }

    public void delete() {
        this.deleteYn = Yn.Y;
    }
}
