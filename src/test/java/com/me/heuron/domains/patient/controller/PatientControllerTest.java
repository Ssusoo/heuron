package com.me.heuron.domains.patient.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.me.heuron.domains.patient.dto.PatientCreateRequest;
import com.me.heuron.domains.test.IntegrationTest;
import com.me.heuron.global.constant.Gender;
import com.me.heuron.global.constant.Yn;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PatientControllerTest extends IntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        super.setup();
        super.getDatabaseCleaner().truncateAll();
    }

    @Test
    @DisplayName("환자 기본 정보 등록 - 성공")
    void create_patient_success() throws Exception {
        // given
        var request = new PatientCreateRequest(
                "김환자",
                20,
                Gender.MALE,
                Yn.N
        );

        // when
        String body = objectMapper.writeValueAsString(request);

        ResultActions result = mockMvc.perform(
                        post("/heuron/patient")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(body)
                )
                .andDo(print());

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.patientSerialNo").exists());
    }

    @Test
    @DisplayName("환자 기본 정보 등록 - 실패 (필수값 누락)")
    void create_patient_fail_validation() throws Exception {
        // given: 나이(null)로 보냄
        var request = new PatientCreateRequest(
                "김환자",
                null,
                com.me.heuron.global.constant.Gender.MALE,
                com.me.heuron.global.constant.Yn.N
        );

        String body = objectMapper.writeValueAsString(request);

        // when
        ResultActions result = mockMvc.perform(
                        post("/heuron/patient")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(body)
                )
                .andDo(print());

        // then
        result.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("환자 상세 조회 - 실패 (존재하지 않는 환자)")
    void get_patient_detail_not_found() throws Exception {
        // when
        ResultActions result = mockMvc.perform(
                        get("/heuron/patient/{patientSerialNo}", 9999L)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print());

        // then
        result.andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("환자 이미지 조회 - 실패 (존재하지 않는 이미지)")
    void get_patient_image_not_found() throws Exception {
        // when
        ResultActions result = mockMvc.perform(
                        get("/heuron/patient/image/{patientImageSerialNo}", 9999L)
                )
                .andDo(print());

        // then
        result.andExpect(status().isNotFound());
    }
}