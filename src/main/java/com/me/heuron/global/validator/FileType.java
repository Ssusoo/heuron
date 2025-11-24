package com.me.heuron.global.validator;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public enum FileType {
	JPEG("jpeg", new String[]{"image/jpeg"}),
	JPG("jpg", new String[]{"image/jpeg"}),
	PNG("png", new String[]{"image/png"}),
	;

	private final String extension;
	private final String[] mimeTypes;
}
