package com.capstone.buddyvet.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.capstone.buddyvet.domain.Post;
import com.capstone.buddyvet.domain.enums.PostState;

public interface PostRepository extends JpaRepository<Post, Long> {
	Optional<Post> findByIdAndState(Long id, PostState state);
}
