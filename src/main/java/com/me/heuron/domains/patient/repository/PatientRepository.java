package com.me.heuron.domains.patient.repository;

import com.me.heuron.base.repository.BaseRepository;
import com.me.heuron.domains.patient.domain.Patient;
import com.me.heuron.domains.patient.domain.QPatient;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class PatientRepository extends BaseRepository<Patient, Long> {
    private final QPatient patient = QPatient.patient;

    public Optional<Patient> findByPatientSerialNo(Long patientSerialNo) {
        return Optional.ofNullable(selectFrom(patient)
                .where(patient.patientSerialNo.eq(patientSerialNo))
                .fetchFirst());
    }
}
