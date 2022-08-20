package com.poc.ilovegithub.core.repository;

import com.poc.ilovegithub.core.domain.UserDetail;
import com.poc.ilovegithub.core.domain.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDetailRepository extends JpaRepository<UserDetail, Integer> {

    Page<UserDetail> findByStatusEquals(UserStatus userStatus, Pageable pageable);

    Page<UserDetail> findByStatusEqualsAndIdGreaterThanAndIdLessThan(UserStatus userStatus, Integer from, Integer to, Pageable pageable);

    // 유저, 조직 RepoInsertJob 에서 조회시 사용
    Page<UserDetail> findByTypeEqualsAndStatusEqualsAndIdGreaterThanAndIdLessThan(String type, UserStatus userStatus, Integer from, Integer to, Pageable pageable);

    // 유저, 조직 RepoInsertJob 에서 조회시 사용(데이터가 많아서 follower 가 특정숫자 이상인 회원만 Repository 검색)
    Page<UserDetail> findByTypeEqualsAndStatusEqualsAndFollowersIsGreaterThanAndIdGreaterThanAndIdLessThan(
            String type, UserStatus userStatus, Integer followers, Integer from, Integer to, Pageable pageable);
}
