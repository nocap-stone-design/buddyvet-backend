package com.capstone.buddyvet.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.capstone.buddyvet.domain.Post;
import com.capstone.buddyvet.domain.enums.PostState;

public interface PostRepository extends JpaRepository<Post, Long> {
	List<Post> findAllByStateOrderByCreatedAtDesc(PostState state);
	Optional<Post> findByIdAndState(Long id, PostState state);
}
