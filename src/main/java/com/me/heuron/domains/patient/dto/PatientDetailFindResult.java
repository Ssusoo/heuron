package com.me.heuron.domains.patient.dto;

import com.me.heuron.domains.patient.domain.Patient;
import com.me.heuron.domains.patient.domain.PatientImage;
import com.me.heuron.global.constant.Gender;
import com.me.heuron.global.constant.Yn;

import java.util.List;

public record PatientDetailFindResult(
        String patientName, // 이름
        int patientAge, // 나이
        Gender patientGender, // 성벌
        Yn patientDiseaseYn, // 질병 여부
        List<Long> patientImageSerialNo,
        List<String> patientImagePaths // 파일 경로
) {
    public PatientDetailFindResult(Patient patient, List<PatientImage> patientImages) {
        this(
                patient.getPatientName(),
                patient.getPatientAge(),
                patient.getPatientGender(),
                patient.getPatientDiseaseYn(),
                patientImages.stream().map(PatientImage::getPatientImageSerialNo).toList(),
                patientImages.stream()
                        .map(img -> "/images/patient/"
                                + patient.getPatientSerialNo()
                                + "/"
                                + img.getPatientImageName())
                        .toList()
        );
    }
}
