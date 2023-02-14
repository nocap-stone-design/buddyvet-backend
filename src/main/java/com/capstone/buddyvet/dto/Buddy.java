package com.capstone.buddyvet.dto;

import java.time.LocalDate;
import java.util.List;

import com.capstone.buddyvet.domain.enums.Gender;
import com.capstone.buddyvet.domain.enums.Kind;

import lombok.Getter;

@Getter
public class Buddy {

	/**
	 * 신규 등록 및 수정 Request
	 */
	@Getter
	public static class SaveRequest {
		// TODO Validation 체크 필요
		private Kind kind;
		private String name;
		private String breed;
		private LocalDate birthday;
		private LocalDate adoptDay;
		private Boolean isNeutered;
		private Gender gender;
	}

	/**
	 * 신규 등록 및 수정 Response
	 */
	@Getter
	public static class SaveResponse {
		private Long buddyId;
	}

	/**
	 * 목록 조회 Response
	 */
	@Getter
	public static class BuddiesResponse {
		private List<Info> buddies;

		@Getter
		public static class Info {
			private Long id;
			private Kind kind;
			private String name;
			private Gender gender;
			private String profile;
			private String age;
		}
	}

	/**
	 * 상세 조회 Response
	 */
	@Getter
	public static class DetailResponse {
		private Info buddy;

		@Getter
		public static class Info {
			private Kind kind;
			private String name;
			private String profile;
			private String breed;
			private LocalDate birthday;
			private LocalDate adoptDay;
			private Boolean isNeutered;
			private Gender gender;
		}
	}
}