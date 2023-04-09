package com.capstone.buddyvet.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.capstone.buddyvet.domain.UserDiaryImage;
import com.capstone.buddyvet.domain.enums.ImageState;

public interface DiaryImageRepository extends JpaRepository<UserDiaryImage, Long> {
	List<UserDiaryImage> findAllByIdInAndState(List<Long> ids, ImageState state);
}
