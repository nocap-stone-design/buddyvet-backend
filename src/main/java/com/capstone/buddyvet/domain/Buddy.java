package com.capstone.buddyvet.domain;

import java.time.LocalDateTime;

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

import lombok.AccessLevel;
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "buddy_breed_id")
	private BuddyBreed buddyBreed;

	@Column(nullable = false, columnDefinition = "VARCHAR(128)")
	private String name;

	private String profile;

	@Column(nullable = false)
	private LocalDateTime birthday;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, columnDefinition = "CHAR(1)")
	private Gender gender;

	@Column(nullable = false, columnDefinition = "TINYINT(1)")
	private boolean neutered;

	@Column(nullable = false)
	private LocalDateTime adoptedAt;

	@Column(nullable = false, columnDefinition = "TINYINT(1)")
	private boolean isActivated;
}
