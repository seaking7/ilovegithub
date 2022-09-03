package com.poc.ilovegithub.core.repository.rank.search;

import com.poc.ilovegithub.core.domain.rank.SearchRankResult;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SearchRankResultRepository extends JpaRepository<SearchRankResult, Integer> {

}
