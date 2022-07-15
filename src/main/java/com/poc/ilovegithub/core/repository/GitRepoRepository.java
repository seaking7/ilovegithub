package com.poc.ilovegithub.core.repository;

import com.poc.ilovegithub.core.domain.GitRepo;
import com.poc.ilovegithub.core.domain.UserDetail;
import com.poc.ilovegithub.core.domain.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GitRepoRepository extends JpaRepository<GitRepo, Integer> {

   List<GitRepo> findAllByLogin(String login);

//   @Query("select login, sum(size) size, sum(stargazers_count) stargazers_count from g_repository gr \n" +
//           "where login = 'abhay' group by login")
//   List<GitRepoGroupDto> findGroupSize(String login);

}
