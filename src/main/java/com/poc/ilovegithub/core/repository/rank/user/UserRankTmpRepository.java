package com.poc.ilovegithub.core.repository.rank.user;

import com.poc.ilovegithub.core.domain.rank.UserRankTmp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRankTmpRepository extends JpaRepository<UserRankTmp, Integer> {

//    Page<UserRankTmp> findAllByMainLanguageIsNull(Pageable pageable);

    Page<UserRankTmp> findAllByFirstLanguageIsNull(Pageable pageable);

}
