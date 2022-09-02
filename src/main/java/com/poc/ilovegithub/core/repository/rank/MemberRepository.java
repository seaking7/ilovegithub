package com.poc.ilovegithub.core.repository.rank;

import com.poc.ilovegithub.core.domain.UserStatus;
import com.poc.ilovegithub.core.domain.rank.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Integer> {

    Page<Member> findByStatusEquals(UserStatus Status, Pageable pageable);
}
