package com.me.heuron.global.error.exception;

/**
 * @SuppressWarnings("unused")
 *  사용하지 않아도 경고 없애줌 (예: IDE에서 경고 뜨는 것 방지)
 */

import com.me.heuron.global.constant.ApiResponseCode;
import lombok.Getter;

/**
 * UnChecked Exception(비검사 예외) : 복구가 불가능한 예외 처리
 *  예외 처리 강제 하지 않음
 *  런타임시 예외 발생
 */
@SuppressWarnings({"serial", "RedundantSuppression"})
public class BusinessRuntimeException extends RuntimeException {
	private final ApiResponseCode apiResponseCode;

	@Getter
	private final String errorMessage;

	public BusinessRuntimeException(ApiResponseCode apiResponseCode) {
		super(apiResponseCode.getMessage());
		this.apiResponseCode = apiResponseCode;
		this.errorMessage = apiResponseCode.getMessage();
	}

	public BusinessRuntimeException(ApiResponseCode apiResponseCode, String message) {
		super(message);
		this.apiResponseCode = apiResponseCode;
		this.errorMessage = message;
	}

	public BusinessRuntimeException(Throwable throwable) {
		super(throwable);
		this.apiResponseCode = ApiResponseCode.INTERNAL_SERVER_ERROR;
		this.errorMessage = throwable.getMessage();
	}

	public ApiResponseCode getErrorCode() {
		return apiResponseCode;
	}
}
