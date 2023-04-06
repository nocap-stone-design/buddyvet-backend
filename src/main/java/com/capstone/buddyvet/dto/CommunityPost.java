package com.capstone.buddyvet.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import com.capstone.buddyvet.domain.Post;
import com.capstone.buddyvet.domain.PostImage;
import com.capstone.buddyvet.domain.Reply;
import com.capstone.buddyvet.domain.User;
import com.capstone.buddyvet.domain.enums.ImageState;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class CommunityPost {

	/**
	 * 목록 조회 Response
	 */
	@Getter
	public static class PostsResponse {
		private List<Info> posts;

		@Getter
		static class Info {
			private Long postId;
			private String title;
			private String thumbnail;
			private int replyCount;

			Info(Post post) {
				this.postId = post.getId();
				this.title = post.getTitle();
				this.thumbnail = post.getPostImages().stream()
					.filter(image -> image.getState().equals(ImageState.ACTIVE))
					.map(PostImage::getUrl)
					.findFirst()
					.orElse(null);
				this.replyCount = post.getReplies().size();
			}
		}

		public PostsResponse(List<Post> posts) {
			this.posts = posts.stream()
				.map(Info::new)
				.collect(Collectors.toList());
		}
	}

	/**
	 * 상세 조회 Response
	 */
	@Getter
	@AllArgsConstructor
	public static class PostDetailResponse {
		private PostDto post;

		@Getter
		@AllArgsConstructor
		@Builder
		static class PostDto {
			private Long id;
			private String title;
			private String content;
			private List<Image> images;
			private LocalDate date;
			private Long authorId;
			private String authorNickname;
			private int replyCount;
			private List<ReplyInfo> reply;

			@Getter
			static class Image {
				private final Long id;
				private final String url;

				public Image(PostImage postImage) {
					this.id = postImage.getId();
					this.url = postImage.getUrl();
				}
			}

			@Getter
			static class ReplyInfo {
				private Long id;
				private Long authorId;
				private String authorNickname;
				private String content;
				private LocalDate date;

				public ReplyInfo(Reply reply) {
					this.id = reply.getId();
					this.authorId = reply.getUser().getId();
					this.authorNickname = reply.getUser().getNickname();
					this.content = reply.getContent();
					this.date = reply.getCreatedAt().toLocalDate();
				}
			}
		}

		public static PostDetailResponse of(Post post) {
			return new PostDetailResponse(
				PostDto.builder()
					.id(post.getId())
					.title(post.getTitle())
					.content(post.getContent())
					.images(
						post.getPostImages().stream()
							.filter(postImage -> postImage.getState().equals(ImageState.ACTIVE))
							.map(PostDto.Image::new)
							.collect(Collectors.toList())
					)
					.date(post.getCreatedAt().toLocalDate())
					.authorId(post.getUser().getId())
					.authorNickname(post.getUser().getNickname())
					.replyCount(post.getReplies().size())
					.reply(
						post.getReplies().stream()
							.map(PostDto.ReplyInfo::new)
							.collect(Collectors.toList())
					)
					.build()
			);
		}
	}

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

	@Getter
	public static class ReplyAddRequest {
		private String content;

		public Reply toEntity(Post post, User user) {
			return Reply.builder()
				.post(post)
				.user(user)
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
		private List<Long> imageIds;
	}
}
