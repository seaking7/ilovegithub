package com.poc.ilovegithub.core.repository;

import com.poc.ilovegithub.core.domain.GithubUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GithubUserRepository extends JpaRepository<GithubUser, Integer> {
}
