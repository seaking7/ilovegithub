package com.poc.ilovegithub.core.service;

import com.poc.ilovegithub.core.domain.rank.OrgRankResult;
import com.poc.ilovegithub.core.domain.rank.OrgRankTmp;
import com.poc.ilovegithub.core.domain.rank.UserRankTmp;
import com.poc.ilovegithub.core.domain.rank.UserRankResult;
import com.poc.ilovegithub.core.repository.GitRepoRepository;
import com.poc.ilovegithub.core.repository.JdbcTemplateRepository;
import com.poc.ilovegithub.core.repository.rank.UserRankTmpRepository;
import com.poc.ilovegithub.core.repository.rank.UserRankResultRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class MainLanguageService {

    GitRepoRepository gitRepoRepository;
    UserRankTmpRepository userRankTmpRepository;
    UserRankResultRepository userRankResultRepository;
    JdbcTemplateRepository jdbcTemplateRepository;

    // user_rank_tmp 에서 데이터 읽어서 main language 3개 찾아서 user_rank_result 에 insert 수행
    public UserRankResult userRankResultMaker(UserRankTmp userRankTmp) {
        UserRankResult returnUserRank = UserRankResult.from(userRankTmp);
        String login = returnUserRank.getLogin();

        returnUserRank.setMainLanguage(findMainLanguage(login));
       return returnUserRank;
    }

    public OrgRankResult orgRankResultMaker(OrgRankTmp orgRankTmp) {
        OrgRankResult returnOrgRank = OrgRankResult.from(orgRankTmp);
        String login = returnOrgRank.getLogin();

        returnOrgRank.setMainLanguage(findMainLanguage(login));
        return returnOrgRank;
    }

    private String findMainLanguage(String login) {
        List<String> mainLanguageByLogin = jdbcTemplateRepository.findMainLanguageByLogin(login);

        String languageConcat = mainLanguageByLogin.stream()
                .collect(Collectors.joining(","));
        return languageConcat;
    }
}
