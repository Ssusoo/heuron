package com.me.heuron.domains.patient.controller;

import com.me.heuron.base.controller.BaseController;
import com.me.heuron.domains.patient.application.PatientCreateService;
import com.me.heuron.domains.patient.application.PatientFindService;
import com.me.heuron.domains.patient.domain.PatientImage;
import com.me.heuron.domains.patient.dto.PatientCreateRequest;
import com.me.heuron.domains.patient.dto.PatientCreateResponse;
import com.me.heuron.domains.patient.dto.PatientDetailFindResult;
import com.me.heuron.global.dto.ApiResponse;
import com.me.heuron.global.validator.FileType;
import com.me.heuron.global.validator.MultipartFileValidation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;

import java.util.List;

@Tag(name = "환자")
@RestController
@RequiredArgsConstructor
@RequestMapping("/heuron/patient")
public class PatientController extends BaseController {
    private final PatientCreateService patientCreateService;
    private final PatientFindService patientFindService;

    @Operation(summary = "환자 기본 정보 등록")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<PatientCreateResponse> createPatient(@Valid @RequestBody PatientCreateRequest request) {
        return ok(patientCreateService.createPatient(request));
    }

    @Operation(summary = "환자 이미지 업로드")
    @PostMapping(value = "/{patientId}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<List<PatientImage>> uploadPatientImage(
            @PathVariable Long patientId,
            @Size(max = 20) @RequestPart(required = false) List<@MultipartFileValidation(
                    allowFileTypes = {FileType.JPEG, FileType.JPG, FileType.PNG},
                    maxUploadSize = 100) MultipartFile> multipartFiles) {
        return ok(patientCreateService.uploadPatientImage(patientId, multipartFiles));
    }

    @Operation(summary = "환자 상세 조회")
    @GetMapping("/{patientSerialNo}")
    public ApiResponse<PatientDetailFindResult> getPatientDetail(@PathVariable Long patientSerialNo) {
        return ok(patientFindService.getPatientDetail(patientSerialNo));
    }

    @Operation(summary = "환자 이미지 조회")
    @GetMapping("/image/{patientImageSerialNo}")
    public ResponseEntity<Resource> getPatientImage(@PathVariable Long patientImageSerialNo) {
        var imageFile = patientFindService.getPatientImage(patientImageSerialNo);

        return ResponseEntity.ok()
                .contentType(imageFile.mediaType())
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline")
                .body(imageFile.resource());
    }
}
