package com.me.heuron.domains.patient.dto;

import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;

public record PatientImageFileResult(
        Resource resource,
        MediaType mediaType
) {}
