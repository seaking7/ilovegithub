package com.poc.ilovegithub.core.repository.rank.search;

import com.poc.ilovegithub.core.domain.rank.SearchRankTmp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SearchRankTmpRepository extends JpaRepository<SearchRankTmp, Integer> {
    Page<SearchRankTmp> findBy(Pageable pageable);
}
