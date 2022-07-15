package com.poc.ilovegithub.core.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.poc.ilovegithub.core.domain.GitRepo;
import com.poc.ilovegithub.core.domain.UserDetail;
import com.poc.ilovegithub.core.domain.UserStatus;
import com.poc.ilovegithub.core.repository.GitRepoRepository;
import com.poc.ilovegithub.core.repository.UserRankRepository;
import com.poc.ilovegithub.dto.GitRepoDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class UserRankInsertService {

    GitRepoRepository gitRepoRepository;
    UserRankRepository userRankRepository;

    public UserDetail userRankInsert(UserDetail userDetail, String gitToken) {

        UserDetail returnUserDetail = userDetail;
        log.info("UserDetail id : {} login: {}", returnUserDetail.getId(), returnUserDetail.getLogin());
//
//        List<GitRepo> allByLogin = gitRepoRepository.findAllByLogin(userDetail.getLogin());
//        for (GitRepo gitRepo : allByLogin) {
//            log.info("{} {} {}", gitRepo.getLogin(), gitRepo.getSize(), gitRepo.getStargazers_count());
//        }
//
//        Map<String, List<GitRepo>> collect = allByLogin.stream()
//                .collect(Collectors.groupingBy(GitRepo::getLanguage, Collectors.toList()));

        return returnUserDetail;

    }


}
