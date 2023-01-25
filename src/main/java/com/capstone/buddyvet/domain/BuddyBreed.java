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

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BuddyBreed {
	@Id
	@Column(name = "buddy_breed_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "buddy_kind_id")
	private BuddyKind buddyKind;

	@OneToMany(mappedBy = "buddyBreed")
	private List<Buddy> buddies = new ArrayList<>();

	@Column(nullable = false, columnDefinition = "VARCHAR(128)")
	private String name;
}
