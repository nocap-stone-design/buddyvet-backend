package com.capstone.buddyvet.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.capstone.buddyvet.domain.User;
import com.capstone.buddyvet.domain.enums.Provider;
import com.capstone.buddyvet.domain.enums.UserState;

public interface UserRepository extends JpaRepository<User, Long> {
	@Query("select u from User u where u.provider = :provider and u.socialId = :socialId and u.state = :state")
	Optional<User> findUserBySocialId(@Param("provider") Provider providerType, @Param("socialId") String socialId,
		@Param("state") UserState state);
}
