package com.capstone.buddyvet.dto;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

import com.capstone.buddyvet.domain.Buddy;
import com.capstone.buddyvet.domain.BuddyBreed;
import com.capstone.buddyvet.domain.User;
import com.capstone.buddyvet.domain.enums.Gender;
import com.capstone.buddyvet.domain.enums.Kind;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Buddies {

	/**
	 * 신규 등록 및 수정 Request
	 */
	@Getter
	public static class SaveRequest {
		private Kind kind;
		private String name;
		private Long breedId;
		private LocalDate birthday;
		private LocalDate adoptDay;
		private Boolean isNeutered;
		private Gender gender;

		public Buddy toEntity(User user, BuddyBreed breed) {
			return Buddy.builder()
				.user(user)
				.buddyBreed(breed)
				.name(name)
				.birthday(birthday)
				.adoptedAt(adoptDay)
				.gender(gender)
				.neutered(isNeutered)
				.build();
		}
	}

	/**
	 * 신규 등록 및 수정 Response
	 */
	@Getter
	@AllArgsConstructor
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

			public Info(Buddy buddy) {
				this.id = buddy.getId();
				this.kind = buddy.getBuddyBreed().getBuddyKind().getKind();
				this.name = buddy.getName();
				this.gender = buddy.getGender();
				this.profile = buddy.getProfile();

				Period period = Period.between(buddy.getBirthday(), LocalDate.now());
				this.age = String.format("%d년 %d개월",
					period.getYears(),
					period.getMonths()
				);
			}
		}

		public BuddiesResponse(List<Buddy> buddies) {
			this.buddies = buddies.stream()
				.map(Info::new)
				.collect(Collectors.toList());
		}
	}

	/**
	 * 상세 조회 Response
	 */
	@Getter
	@AllArgsConstructor
	public static class DetailResponse {
		private Info buddy;

		@Getter
		@AllArgsConstructor
		@Builder
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

		public static DetailResponse of(Buddy buddy) {
			return new DetailResponse(
				Info.builder()
					.kind(buddy.getBuddyBreed().getBuddyKind().getKind())
					.name(buddy.getName())
					.profile(buddy.getProfile())
					.breed(buddy.getBuddyBreed().getName())
					.birthday(buddy.getBirthday())
					.adoptDay(buddy.getAdoptedAt())
					.isNeutered(buddy.isNeutered())
					.build()
			);
		}
	}
}