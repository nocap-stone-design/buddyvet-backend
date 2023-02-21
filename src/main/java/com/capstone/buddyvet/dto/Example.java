package com.capstone.buddyvet.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.capstone.buddyvet.domain.UserDiary;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Example {

	@Getter
	public static class DiariesResponse {
		private List<Info> diaries;

		@Getter
		@AllArgsConstructor
		@Builder
		public static class Info {
			private Long diaryId;
			private String thumbnail;

			public Info(UserDiary diary) {
				this.diaryId = diary.getId();
			}
		}

		public DiariesResponse(List<UserDiary> diaries) {
			this.diaries = diaries.stream()
				.map(Info::new)
				.collect(Collectors.toList());
		}
	}
}
