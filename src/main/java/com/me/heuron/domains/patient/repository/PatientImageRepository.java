package com.me.heuron.domains.patient.repository;

import com.me.heuron.base.repository.BaseRepository;
import com.me.heuron.domains.patient.domain.PatientImage;
import com.me.heuron.domains.patient.domain.QPatientImage;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class PatientImageRepository extends BaseRepository<PatientImage, Long> {
    private final QPatientImage patientImage = QPatientImage.patientImage;

    public List<PatientImage> findByPatientImages(Long patientSerialNo) {
        return selectFrom(patientImage).where(patientImage.patient.patientSerialNo.in(patientSerialNo)).fetch();
    }

    public Optional<PatientImage> findByPatientImageSerialNo(Long patientImageSerialNo) {
        return Optional.ofNullable(selectFrom(patientImage)
                .where(patientImage.patientImageSerialNo.eq(patientImageSerialNo))
                .fetchFirst());
    }
}
