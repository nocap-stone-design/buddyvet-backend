package com.capstone.buddyvet.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.capstone.buddyvet.domain.Buddy;

public interface BuddyRepository extends JpaRepository<Buddy, Long> {
}
