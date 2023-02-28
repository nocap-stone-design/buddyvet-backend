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
import com.capstone.buddyvet.domain.enums.DiaryState;
import com.capstone.buddyvet.dto.Diary;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserDiary extends BaseTimeEntity {
	@Id
	@Column(name = "user_diary_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@Column(nullable = false)
	private LocalDate date;

	@Column(nullable = false)
	private String title;

	@Column(nullable = false, columnDefinition = "TEXT")
	private String content;

	@OneToMany(mappedBy = "userDiary", cascade = CascadeType.ALL)
	private List<UserDiaryImage> userDiaryImages = new ArrayList<>();

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, columnDefinition = "CHAR(8)")
	private DiaryState state;

	@Builder
	public UserDiary(User user, LocalDate date, String title, String content) {
		this.user = user;
		this.date = date;
		this.title = title;
		this.content = content;
		this.state = DiaryState.ACTIVE;
	}

	//==비즈니스 로직==//
	public void delete() {
		this.state = DiaryState.DELETED;
	}

	public void saveImage(UserDiaryImage diaryImage) {
		this.userDiaryImages.add(diaryImage);
		diaryImage.setDiary(this);
	}

	public void update(Diary.AddRequest request) {
		this.date = request.getDate();
		this.title = request.getTitle();
		this.content = request.getContent();
	}
}
