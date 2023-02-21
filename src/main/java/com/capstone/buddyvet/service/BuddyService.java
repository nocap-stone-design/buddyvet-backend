package com.capstone.buddyvet.service;

import org.springframework.stereotype.Service;

import com.capstone.buddyvet.dto.Buddy.BuddiesResponse;
import com.capstone.buddyvet.dto.Buddy.DetailResponse;
import com.capstone.buddyvet.dto.Buddy.SaveRequest;
import com.capstone.buddyvet.dto.Buddy.SaveResponse;
import com.capstone.buddyvet.repository.BuddyRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class BuddyService {

	private final BuddyRepository buddyRepository;

	public BuddiesResponse getBuddies() {
		return new BuddiesResponse();
	}

	public SaveResponse addBuddy(SaveRequest request) {
		return new SaveResponse();
	}

	public DetailResponse getBuddy(Long buddyId) {
		return new DetailResponse();
	}

	public SaveResponse modifyBuddy(Long buddyId, SaveRequest request) {
		return new SaveResponse();
	}

	public Object removeBuddy(Long buddyId) {
		return null;
	}
}
