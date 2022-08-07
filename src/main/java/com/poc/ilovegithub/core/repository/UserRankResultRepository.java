package com.poc.ilovegithub.core.repository;

import com.poc.ilovegithub.core.domain.UserRank;
import com.poc.ilovegithub.core.domain.UserRankResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRankResultRepository extends JpaRepository<UserRankResult, Long> {

}
