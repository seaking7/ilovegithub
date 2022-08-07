package com.poc.ilovegithub.core.repository;

import com.poc.ilovegithub.core.domain.GitRepo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GitRepoRepository extends JpaRepository<GitRepo, Integer> {

   List<GitRepo> findAllByLogin(String login);



}
