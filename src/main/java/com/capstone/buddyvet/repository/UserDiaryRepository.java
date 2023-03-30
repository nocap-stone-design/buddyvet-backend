package com.capstone.buddyvet.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.capstone.buddyvet.domain.User;
import com.capstone.buddyvet.domain.UserDiary;
import com.capstone.buddyvet.domain.enums.DiaryState;

public interface UserDiaryRepository extends JpaRepository<UserDiary, Long> {
	List<UserDiary> findAllByUserAndStateAndDateBetween(User user, DiaryState state, LocalDate start, LocalDate end);
}
