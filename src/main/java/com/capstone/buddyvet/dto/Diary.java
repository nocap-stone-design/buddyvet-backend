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
	 * 목록 조회 Response
	 */
	@Getter
	public static class DiariesResponse {
		private String year;
		private String month;
		private List<Info> diaries;

		@Getter
		@AllArgsConstructor
		@Builder
		public static class Info {
			private int day;
			private Long diaryId;
			private String thumbnail;

			/**
			 * TODO N+1 문제 생기는지 확인 필요.
			 * 쿼리 단에서 ACTIVE 상태인 image 를 가져올 수 있도록 변경할것.
			 */
			public Info(UserDiary diary) {
				this.day = diary.getDate().getDayOfMonth();
				this.diaryId = diary.getId();
				this.thumbnail = diary.getUserDiaryImages().stream()
					.filter(image -> image.getState().equals(ImageState.ACTIVE))
					.map(UserDiaryImage::getUrl)
					.findFirst()
					.orElse(null);
			}
		}

		public DiariesResponse(LocalDate date, List<UserDiary> diaries) {
			this.year = Integer.toString(date.getYear());
			this.month = Integer.toString(date.getMonthValue());
			this.diaries = diaries.stream()
				.map(Info::new)
				.collect(Collectors.toList());
		}
	}

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
		private List<Long> imageIds;
	}
}
