package com.capstone.buddyvet.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.capstone.buddyvet.common.domain.BaseTimeEntity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DiseaseInfo extends BaseTimeEntity {
	@Id
	@Column(name = "disease_info_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "disease_id")
	private Disease disease;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_check_log_id")
	private UserCheckLog userCheckLog;

	@Column(nullable = false)
	private int percentage;
}
