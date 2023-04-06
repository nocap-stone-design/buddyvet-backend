package com.capstone.buddyvet.domain;

import java.time.LocalDate;

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

import com.capstone.buddyvet.common.domain.BaseTimeEntity;
import com.capstone.buddyvet.domain.enums.Gender;
import com.capstone.buddyvet.domain.enums.Kind;
import com.capstone.buddyvet.dto.Buddies;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Buddy extends BaseTimeEntity {
	@Id
	@Column(name = "buddy_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@Column(nullable = false, columnDefinition = "VARCHAR(128)")
	private String name;

	private String profile;

	@Column(nullable = false)
	private LocalDate birthday;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, columnDefinition = "CHAR(1)")
	private Gender gender;

	@Column(nullable = false, columnDefinition = "TINYINT(1)")
	private boolean neutered;

	@Column(nullable = false)
	private LocalDate adoptedAt;

	@Column(nullable = false, columnDefinition = "TINYINT(1)")
	private boolean activated;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, columnDefinition = "CHAR(1)")
	private Kind kind;

	@Builder
	public Buddy(User user, String name, String profile, LocalDate birthday, Gender gender,
		boolean neutered, LocalDate adoptedAt, Kind kind) {
		this.user = user;
		this.name = name;
		this.profile = profile;
		this.birthday = birthday;
		this.gender = gender;
		this.neutered = neutered;
		this.activated = true;
		this.adoptedAt = adoptedAt;
		this.kind = kind;
	}

	//==비즈니스 로직==//
	public void delete() {
		this.activated = false;
	}

	public void saveImage(String url) {
		this.profile = url;
	}

	public void modify(Buddies.SaveRequest request) {
		if (request.getKind() != null) this.kind = request.getKind();
		if (request.getName() != null) this.name = request.getName();
		if (request.getBirthday() != null) this.birthday = request.getBirthday();
		if (request.getAdoptDay() != null) this.adoptedAt = request.getAdoptDay();
		if (request.getIsNeutered() != null) this.neutered = request.getIsNeutered();
		if (request.getGender() != null) this.gender = request.getGender();
	}
}
