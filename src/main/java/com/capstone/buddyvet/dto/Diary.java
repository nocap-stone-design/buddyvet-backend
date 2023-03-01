package com.capstone.buddyvet.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import com.capstone.buddyvet.domain.User;
import com.capstone.buddyvet.domain.UserDiary;
import com.capstone.buddyvet.domain.UserDiaryImage;
import com.capstone.buddyvet.domain.enums.ImageState;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Diary {

	/**
	 * 신규 작성 Request
	 */
	@Getter
	public static class AddRequest {
		private LocalDate date;
		private String title;
		private String content;

		//==DTO 변환 메소드==//
		public UserDiary toEntity(User user) {
			return UserDiary.builder()
				.user(user)
				.date(date)
				.title(title)
				.content(content)
				.build();
		}
	}

	/**
	 * 신규 작성 Response
	 */
	@Getter
	@AllArgsConstructor
	public static class AddResponse {
		private Long diaryId;
	}

	/**
	 * 상세 조회 Response
	 */
	@Getter
	@AllArgsConstructor
	@Builder
	public static class DetailResponse {
		private LocalDate date;
		private String title;
		private List<Image> images;
		private String content;

		@Getter
		public static class Image {
			private Long id;
			private String url;

			public Image(UserDiaryImage diaryImage) {
				this.id = diaryImage.getId();
				this.url = diaryImage.getUrl();
			}
		}

		//==DTO 변환 메소드==//
		public static DetailResponse of(UserDiary diary) {
			return DetailResponse.builder()
				.date(diary.getDate())
				.title(diary.getTitle())
				.images(
					diary.getUserDiaryImages().stream()
						.filter(diaryImage -> diaryImage.getState() == ImageState.ACTIVE)
						.map(Image::new)
						.collect(Collectors.toList())
				)
				.content(diary.getContent())
				.build();
		}
	}

	/**
	 * 사진 삭제 Request
	 */
	@Getter
	public static class ImageRemoveRequest {
		private Long imageId;
	}
}
