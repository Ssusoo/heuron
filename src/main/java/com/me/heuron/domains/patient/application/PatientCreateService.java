package com.me.heuron.domains.patient.application;

import com.me.heuron.domains.patient.domain.Patient;
import com.me.heuron.domains.patient.domain.PatientImage;
import com.me.heuron.domains.patient.dto.PatientCreateRequest;
import com.me.heuron.domains.patient.dto.PatientCreateResponse;
import com.me.heuron.domains.patient.repository.PatientImageRepository;
import com.me.heuron.domains.patient.repository.PatientRepository;
import com.me.heuron.global.error.exception.DataNotFoundException;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientCreateService {
    private final PatientRepository patientRepository;
    private final PatientImageRepository patientImageRepository;

    // 실제로 파일을 쓸 base path (classpath:/images 디렉터리)
    private Path uploadBasePath;

    @Transactional
    public PatientCreateResponse createPatient(@Valid PatientCreateRequest request) {
        // 환자 정보 저장
        Patient patient = patientRepository.save(Patient.create(request));
        return new PatientCreateResponse(patient.getPatientSerialNo());
    }

    @PostConstruct
    public void initUploadBasePath() throws IOException {
        // classpath 의 images 디렉터리 기준 경로 얻기
        ClassPathResource resource = new ClassPathResource("images");
        File dir = resource.getFile();   // IDE/로컬 실행 환경에서 실제 디렉터리로 매핑됨

        if (!dir.exists()) {
            // 혹시 빌드 아웃풋에 없으면 한 번 만들어 줌
            if (!dir.mkdirs()) {
                throw new IllegalStateException("images 디렉터리를 생성할 수 없습니다.");
            }
        }
        this.uploadBasePath = dir.toPath();  // 예: {프로젝트}/target/classes/images
    }

    @Transactional
    public List<PatientImage> uploadPatientImage(Long patientSerialNo, List<MultipartFile> multipartFiles) {
        Patient patient = patientRepository.findByPatientSerialNo(patientSerialNo)
                .orElseThrow(DataNotFoundException::new);

        if (multipartFiles == null ||
                multipartFiles.isEmpty() ||
                multipartFiles.stream().allMatch(MultipartFile::isEmpty)) {
            return Collections.emptyList();
        }

        // heuron/build/resources/main/images/patient/1
        Path patientPath = uploadBasePath.resolve("patient").resolve(String.valueOf(patientSerialNo));
        try {
            Files.createDirectories(patientPath);
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 경로 생성 중 오류가 발생했습니다.", e);
        }

        List<PatientImage> savedImages = new ArrayList<>();

        for (MultipartFile file : multipartFiles) {
            if (file == null || file.isEmpty()) continue;

            String originalName = file.getOriginalFilename();
            if (originalName == null || originalName.isBlank()) continue;

            String extension = extractExtension(originalName);
            String storedFileName = originalName;

            Path targetPath = patientPath.resolve(extension);

            try {
                file.transferTo(targetPath.toFile());
            } catch (IOException e) {
                throw new RuntimeException("파일 저장 중 오류가 발생했습니다.", e);
            }
            PatientImage saved = patientImageRepository.save(PatientImage.create(patient, patientPath, storedFileName, originalName, extension, file));
            savedImages.add(saved);
        }
        return savedImages;
    }

    private String extractExtension(String fileName) {
        int idx = fileName.lastIndexOf('.');
        if (idx == -1) {
            throw new IllegalArgumentException("파일 확장자가 없습니다.");
        }
        return fileName.substring(idx + 1).toLowerCase();
    }
}
