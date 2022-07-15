package com.poc.ilovegithub.core.repository;

import com.poc.ilovegithub.core.domain.UserRank;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRankRepository extends JpaRepository<UserRank, Long> {


}
