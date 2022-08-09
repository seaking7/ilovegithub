package com.poc.ilovegithub.core.repository.rank;

import com.poc.ilovegithub.core.domain.rank.OrgRankTmp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrgRankTmpRepository extends JpaRepository<OrgRankTmp, Integer> {

    Page<OrgRankTmp> findBy(Pageable pageable);

}
