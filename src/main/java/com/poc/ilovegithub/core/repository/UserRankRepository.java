package com.poc.ilovegithub.core.repository;

import com.poc.ilovegithub.core.domain.UserRank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRankRepository extends JpaRepository<UserRank, Long> {

    Page<UserRank> findAllByMainLanguageIsNull(Pageable pageable);


}
