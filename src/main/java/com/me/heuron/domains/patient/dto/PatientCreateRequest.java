package com.me.heuron.domains.patient.dto;

import com.me.heuron.global.constant.Gender;
import com.me.heuron.global.constant.Yn;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PatientCreateRequest(
        @Schema(description = "환자 이름", example = "김환자")
        @NotBlank(message = "환자 이름은 필수입니다.")
        String patientName,

        @Schema(description = "환자 나이", example = "20")
        @NotNull(message = "환자 나이는 필수입니다.")
        Integer patientAge,

        @Schema(description = "환자 성별", example = "MALE", allowableValues = {"MALE", "FEMALE"})
        @NotNull(message = "환자 성별은 필수입니다.")
        Gender patientGender,

        @Schema(description = "환자 질병 여부", example = "Y", allowableValues = {"Y", "N"})
        @NotNull(message = "환자 질병 여부는 필수입니다.")
        Yn patientDiseaseYn
) {
}
