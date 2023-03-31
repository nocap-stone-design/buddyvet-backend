package com.capstone.buddyvet.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.capstone.buddyvet.domain.UserDiary;
import com.capstone.buddyvet.domain.enums.DiaryState;

public interface DiaryRepository extends JpaRepository<UserDiary, Long> {
	Optional<UserDiary> findByIdAndState(Long id, DiaryState state);
}
