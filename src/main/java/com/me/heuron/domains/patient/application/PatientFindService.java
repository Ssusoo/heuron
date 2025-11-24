package com.me.heuron.domains.patient.application;

import com.me.heuron.domains.patient.domain.PatientImage;
import com.me.heuron.domains.patient.dto.PatientDetailFindResult;
import com.me.heuron.domains.patient.dto.PatientImageFileResult;
import com.me.heuron.domains.patient.repository.PatientImageRepository;
import com.me.heuron.domains.patient.repository.PatientRepository;
import com.me.heuron.global.error.exception.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.http.MediaType;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PatientFindService {
    private final PatientImageRepository patientImageRepository;
    private final PatientRepository patientRepository;

    public PatientDetailFindResult getPatientDetail(Long patientSerialNo) {
        // 환자 정보 조회
        var patient = patientRepository.findByPatientSerialNo(patientSerialNo).orElseThrow(DataNotFoundException::new);

        // 환자 이미지 조회
        List<PatientImage> patientImages = patientImageRepository.findByPatientImages(patient.getPatientSerialNo());

        if (patientImages == null || patientImages.isEmpty()) {
            throw new DataNotFoundException();
        }

        return new PatientDetailFindResult(patient, patientImages);
    }

    public PatientImageFileResult getPatientImage(Long patientImageSerialNo) {
        // 1) DB에서 메타 정보 조회
        PatientImage patientImage = patientImageRepository.findByPatientImageSerialNo(patientImageSerialNo)
                .orElseThrow(DataNotFoundException::new);

        // 2) 실제 파일 경로
        Path filePath = Paths.get(
                patientImage.getPatientImagePath(),
                patientImage.getPatientImageName()
        );

        UrlResource resource;
        try {
            resource = new UrlResource(filePath.toUri());
        } catch (MalformedURLException e) {
            throw new RuntimeException("이미지 파일 경로가 올바르지 않습니다.", e);
        }

        if (!resource.exists()) {
            throw new DataNotFoundException();
        }

        // 3) Content-Type 설정
        String ext = patientImage.getPatientImageExtension();
        MediaType mediaType = MediaType.IMAGE_JPEG;
        if ("png".equalsIgnoreCase(ext)) {
            mediaType = MediaType.IMAGE_PNG;
        }

        // 4) 컨트롤러로 넘길 DTO
        return new PatientImageFileResult(resource, mediaType);
    }
}
