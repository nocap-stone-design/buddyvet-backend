package com.capstone.buddyvet.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.capstone.buddyvet.domain.Buddy;
import com.capstone.buddyvet.domain.User;

public interface BuddyRepository extends JpaRepository<Buddy, Long> {
	List<Buddy> findAllByUserAndActivated(User user, boolean isActivated);

	Optional<Buddy> findByIdAndUserAndActivated(Long id, User user, boolean isActivated);
}
