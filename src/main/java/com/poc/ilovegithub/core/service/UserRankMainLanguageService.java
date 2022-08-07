package com.poc.ilovegithub.core.service;

import com.poc.ilovegithub.core.domain.UserDetail;
import com.poc.ilovegithub.core.domain.UserRank;
import com.poc.ilovegithub.core.domain.UserRankResult;
import com.poc.ilovegithub.core.repository.GitRepoRepository;
import com.poc.ilovegithub.core.repository.JdbcTemplateRepository;
import com.poc.ilovegithub.core.repository.UserRankRepository;
import com.poc.ilovegithub.core.repository.UserRankResultRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class UserRankMainLanguageService {

    GitRepoRepository gitRepoRepository;
    UserRankRepository userRankRepository;
    UserRankResultRepository userRankResultRepository;
    JdbcTemplateRepository jdbcTemplateRepository;

    public UserRankResult userRankInsert(UserRank userRank) {

        UserRankResult returnUserRank = UserRankResult.from(userRank);

        List<String> mainLanguageById = jdbcTemplateRepository.findMainLanguageById(returnUserRank.getId());

        String languageConcat = mainLanguageById.stream()
                .collect(Collectors.joining(","));
        log.info("UserDetail id : {} login: {}, main: {}", returnUserRank.getId(), returnUserRank.getLogin(), languageConcat);

       returnUserRank.setMainLanguage(languageConcat);

       return returnUserRank;


    }

}
