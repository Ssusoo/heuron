package com.me.heuron.global.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tika.Tika;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;

/**
 * MultipartFile 유효성 검증
 */
public class MultipartFileValidator implements ConstraintValidator<MultipartFileValidation, MultipartFile> {
	private MultipartFileValidation annotation;

	@Override
	public void initialize(MultipartFileValidation constraintAnnotation) {
		this.annotation = constraintAnnotation;
	}

	@Override
	public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext context) {
		if (multipartFile.isEmpty()) {
			context.buildConstraintViolationWithTemplate("파일이 없습니다.").addConstraintViolation();
			return false;
		}
		final var fileName = multipartFile.getOriginalFilename();
		if (StringUtils.isBlank(fileName)) {
			context.buildConstraintViolationWithTemplate("파일명이 존재하지 않습니다.").addConstraintViolation();
			return false;
		}
		// 업로드 용량 검증
		var maxUploadSize = annotation.maxUploadSize() * 1024 * 1024; // 10MB
		if (multipartFile.getSize() > maxUploadSize) {
			context.buildConstraintViolationWithTemplate(
					String.format("업로드 허용 용량을 초과했습니다. 최대 %dMB 까지 업로드 가능합니다.", maxUploadSize)).addConstraintViolation();
			return false;
		}
		// 파일 유형 검증
		final var detectedMediaType = getMimeType(multipartFile); // 확장자 변조한 파일인지 확인을 위한 mime type 얻기
		final var allowFileTypes = annotation.allowFileTypes();
		final var fileExt = FilenameUtils.getExtension(fileName);
		if (Arrays.stream(allowFileTypes)
				.noneMatch(fileType -> fileType.getExtension().equals(fileExt)
						&& ArrayUtils.contains(fileType.getMimeTypes(), detectedMediaType))) {
			context.buildConstraintViolationWithTemplate("업로드가 허용되지 않는 파일 유형입니다.").addConstraintViolation();
			return false;
		}
		return true;
	}

	/**
	 * 파일의 mimeType 반환
	 */
	private String getMimeType(MultipartFile multipartFile) {
		try {
			var tika = new Tika();
			return tika.detect(multipartFile.getInputStream());
		} catch (IOException e) {
			return null;
		}
	}
}
