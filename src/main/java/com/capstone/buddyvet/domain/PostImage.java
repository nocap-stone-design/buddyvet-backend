package com.capstone.buddyvet.domain;

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

import org.hibernate.annotations.DynamicInsert;

import com.capstone.buddyvet.common.domain.BaseTimeEntity;
import com.capstone.buddyvet.domain.enums.ImageState;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostImage extends BaseTimeEntity {
	@Id
	@Column(name = "post_image_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_id")
	private Post post;

	@Column(nullable = false)
	private String url;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, columnDefinition = "CHAR(8)")
	private ImageState state;

	//==비즈니스 로직==//
	public void delete() {
		state = ImageState.DELETED;
	}
}
