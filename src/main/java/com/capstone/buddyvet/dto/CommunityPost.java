package com.capstone.buddyvet.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import com.capstone.buddyvet.domain.Post;
import com.capstone.buddyvet.domain.PostImage;
import com.capstone.buddyvet.domain.User;
import com.capstone.buddyvet.domain.enums.ImageState;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class CommunityPost {

	/**
	 * 신규 작성 Request
	 */
	@Getter
	public static class AddRequest {
		private String title;
		private String content;

		//==DTO 변환 메소드==//
		public Post toEntity(User user) {
			return Post.builder()
				.user(user)
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
		private Long postId;
	}

	/**
	 * 사진 삭제 Request
	 */
	@Getter
	public static class ImageRemoveRequest {
		private Long imageId;
	}
}
