package com.capstone.buddyvet.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.capstone.buddyvet.domain.UserDiaryImage;
import com.capstone.buddyvet.domain.enums.ImageState;

public interface DiaryImageRepository extends JpaRepository<UserDiaryImage, Long> {
	Optional<UserDiaryImage> findByIdAndState(Long id, ImageState state);
}
