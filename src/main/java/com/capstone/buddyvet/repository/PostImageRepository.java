package com.capstone.buddyvet.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.capstone.buddyvet.domain.PostImage;
import com.capstone.buddyvet.domain.enums.ImageState;

public interface PostImageRepository extends JpaRepository<PostImage, Long> {
	Optional<PostImage> findByIdAndState(Long id, ImageState state);
}
