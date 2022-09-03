package com.poc.ilovegithub.core.repository.rank.member;

import com.poc.ilovegithub.core.domain.rank.MemberRankTmp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRankTmpRepository extends JpaRepository<MemberRankTmp, Integer> {


    Page<MemberRankTmp> findBy(Pageable pageable);

}
