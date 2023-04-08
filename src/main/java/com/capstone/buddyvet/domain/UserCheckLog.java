package com.capstone.buddyvet.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.capstone.buddyvet.common.domain.BaseTimeEntity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserCheckLog extends BaseTimeEntity {
	@Id
	@Column(name = "user_check_log_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "buddy_id")
	private Buddy buddy;

	@OneToMany(mappedBy = "userCheckLog")
	private List<DiseaseInfo> diseaseInfos = new ArrayList<>();

	public UserCheckLog(Buddy buddy) {
		this.buddy = buddy;
	}
}
