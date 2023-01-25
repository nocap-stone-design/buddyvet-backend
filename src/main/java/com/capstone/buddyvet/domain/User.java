package com.capstone.buddyvet.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import com.capstone.buddyvet.common.domain.BaseTimeEntity;
import com.capstone.buddyvet.domain.enums.Provider;
import com.capstone.buddyvet.domain.enums.UserState;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {
	@Id
	@Column(name = "user_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 64)
	private String nickname;

	@Column(columnDefinition = "TEXT")
	private String description;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, columnDefinition = "CHAR(16)")
	@ColumnDefault("ACTIVE")
	private UserState state;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, columnDefinition = "CHAR(8)")
	private Provider provider;

	@Column(nullable = false)
	private String socialId;

	private String fcmToken;

	@Column(nullable = false, columnDefinition = "TINYINT(1)")
	private boolean isAllowPush;

	@OneToMany(mappedBy = "user")
	private List<Buddy> buddies = new ArrayList<>();

	@OneToMany(mappedBy = "user")
	private List<UserDiary> userDiaries = new ArrayList<>();

	@OneToMany(mappedBy = "user")
	private List<Post> posts = new ArrayList<>();
}
