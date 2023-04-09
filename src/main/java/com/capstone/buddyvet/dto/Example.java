package com.capstone.buddyvet.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import com.capstone.buddyvet.domain.Post;
import com.capstone.buddyvet.domain.PostImage;
import com.capstone.buddyvet.domain.UserDiary;
import com.capstone.buddyvet.domain.UserDiaryImage;
import com.capstone.buddyvet.domain.enums.ImageState;

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

			/**
			 * TODO N+1 문제 생기는지 확인 필요.
			 * 실제 구현 시에는 쿼리 단에서 ACTIVE 상태인 image 를 가져올 수 있도록 변경할것.
			 */
			public Info(UserDiary diary) {
				this.diaryId = diary.getId();
				this.thumbnail = diary.getUserDiaryImages().stream()
					.filter(image -> image.getState().equals(ImageState.ACTIVE))
					.map(UserDiaryImage::getUrl)
					.findFirst()
					.orElse(null);
			}
		}

		public DiariesResponse(List<UserDiary> diaries) {
			this.diaries = diaries.stream()
				.map(Info::new)
				.collect(Collectors.toList());
		}
	}
}
