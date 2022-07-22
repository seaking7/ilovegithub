package com.poc.ilovegithub.core.repository;

import com.poc.ilovegithub.core.domain.GithubUser;
import com.poc.ilovegithub.core.domain.UserDetail;
import com.poc.ilovegithub.core.domain.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserDetailRepository extends JpaRepository<UserDetail, Integer> {

    Page<UserDetail> findByStatusEquals(UserStatus userStatus, Pageable pageable);

    Page<UserDetail> findByStatusEqualsAndIdGreaterThanAndIdLessThan(UserStatus userStatus, Integer from, Integer to, Pageable pageable);

}
