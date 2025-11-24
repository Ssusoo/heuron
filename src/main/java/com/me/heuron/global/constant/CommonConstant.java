package com.me.heuron.global.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommonConstant {
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class Yn {
		public static final String Y = "Y";
		public static final String N = "N";
	}

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class PatientGender {
        public static final String M = "M";
        public static final String F = "F";
    }
}
