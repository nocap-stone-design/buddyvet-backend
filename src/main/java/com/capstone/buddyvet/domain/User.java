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

import com.capstone.buddyvet.common.domain.BaseTimeEntity;
import com.capstone.buddyvet.domain.enums.Provider;
import com.capstone.buddyvet.domain.enums.RoleType;
import com.capstone.buddyvet.domain.enums.UserState;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {
	@Id
	@Column(name = "user_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 64)
	private String nickname;

	@Column(columnDefinition = "TEXT")
	private String description;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, columnDefinition = "CHAR(16)")
	@ColumnDefault("'ACTIVE'")
	private UserState state;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, columnDefinition = "CHAR(8)")
	private Provider provider;

	@Column(nullable = false)
	private String socialId;

	@Enumerated(EnumType.STRING)
	private RoleType roleType;

	private String fcmToken;

	@Column(nullable = false, columnDefinition = "TINYINT(1)")
	private boolean isAllowPush;

	@OneToMany(mappedBy = "user")
	private List<Buddy> buddies = new ArrayList<>();

	@OneToMany(mappedBy = "user")
	private List<UserDiary> userDiaries = new ArrayList<>();

	@OneToMany(mappedBy = "user")
	private List<Post> posts = new ArrayList<>();

	@Builder
	public User(String socialId, String nickname) {
		this.state = UserState.ACTIVE;
		this.provider = Provider.KAKAO;
		this.socialId = socialId;
		this.nickname = nickname;
		this.isAllowPush = false;
		this.roleType = RoleType.USER;
	}

	public boolean isActivate() {
		return this.state == UserState.ACTIVE;
	}
}
