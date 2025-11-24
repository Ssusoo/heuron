package com.me.heuron.global.appication;

import com.me.heuron.global.constant.code.CodeEnum;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.apache.commons.lang3.StringUtils;

import java.util.EnumSet;
import java.util.NoSuchElementException;

@Converter
public class CodeEnumConverter<E extends Enum<E> & CodeEnum> implements AttributeConverter<E, String> {
	private final Class<E> classOfE;

	protected CodeEnumConverter(Class<E> enumClass) {
		this.classOfE = enumClass;
	}

	@Override
	public String convertToDatabaseColumn(E attribute) {
		return (attribute != null) ? attribute.getCode() : null;
	}

	@Override
	public E convertToEntityAttribute(String value) {
		if (StringUtils.isEmpty(value)) {
			return null;
		}
		return EnumSet.allOf(classOfE).stream()
				.filter(e -> e.getCode().equals(value))
				.findAny()
				.orElseThrow(NoSuchElementException::new);
	}
}
