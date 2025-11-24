package com.me.heuron.domains.patient.dto;

public record ImageItem(
        String patientImageType,
        String patientImagePath,
        String patientOriginalImageName,
        String patientImageName,
        String patientImageExtension,
        String fileUrl
) {
    public ImageItem(String filePathways, String fileName) {
        this(
                null,
                filePathways,
                null,
                fileName,
                null,
                null
        );
    }
}
