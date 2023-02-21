package com.capstone.buddyvet.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.capstone.buddyvet.common.domain.BaseTimeEntity;
import com.capstone.buddyvet.domain.enums.PostState;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseTimeEntity {
	@Id
	@Column(name = "post_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
	private List<PostImage> postImages = new ArrayList<>();

	@Column(nullable = false)
	private LocalDate date;

	@Column(nullable = false, columnDefinition = "VARCHAR(128)")
	private String title;

	@Column(nullable = false, columnDefinition = "TEXT")
	private String content;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, columnDefinition = "CHAR(8)")
	private PostState state;

	@Builder
	public Post(User user, LocalDate date, String title, String content) {
		this.user = user;
		this.date = date;
		this.title = title;
		this.content = content;
		this.state = PostState.ACTIVE;
	}

	//==비즈니스 로직==//
	public void delete() {
		this.state = PostState.DELETED;
	}

	public void saveImage(PostImage postImage) {
		this.postImages.add(postImage);
		postImage.setPost(this);
	}
}
