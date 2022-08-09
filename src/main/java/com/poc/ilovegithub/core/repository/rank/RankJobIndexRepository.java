package com.poc.ilovegithub.core.repository.rank;

import com.poc.ilovegithub.core.domain.rank.RankJobIndex;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RankJobIndexRepository extends JpaRepository<RankJobIndex, Integer> {

    Page<RankJobIndex> findByRankTableEquals(String rankTable, Pageable pageable);

}
