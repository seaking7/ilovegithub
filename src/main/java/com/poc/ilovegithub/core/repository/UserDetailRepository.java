package com.poc.ilovegithub.core.repository;

import com.poc.ilovegithub.core.domain.UserDetail;
import com.poc.ilovegithub.core.domain.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDetailRepository extends JpaRepository<UserDetail, Integer> {

    Page<UserDetail> findByStatusEquals(UserStatus userStatus, Pageable pageable);

    Page<UserDetail> findByStatusEqualsAndIdGreaterThanAndIdLessThan(UserStatus userStatus, Integer from, Integer to, Pageable pageable);

    Page<UserDetail> findByTypeEqualsAndStatusEqualsAndIdGreaterThanAndIdLessThan(String type, UserStatus userStatus, Integer from, Integer to, Pageable pageable);

}
