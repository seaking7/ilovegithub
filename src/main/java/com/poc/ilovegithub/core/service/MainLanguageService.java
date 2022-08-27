package com.poc.ilovegithub.core.service;

import com.poc.ilovegithub.core.domain.rank.OrgRankResult;
import com.poc.ilovegithub.core.domain.rank.OrgRankTmp;
import com.poc.ilovegithub.core.domain.rank.UserRankResult;
import com.poc.ilovegithub.core.domain.rank.UserRankTmp;
import com.poc.ilovegithub.core.repository.GitRepoRepository;
import com.poc.ilovegithub.core.repository.JdbcTemplateRepository;
import com.poc.ilovegithub.core.repository.rank.UserRankResultRepository;
import com.poc.ilovegithub.core.repository.rank.UserRankTmpRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

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

        List<String> mainLanguageByLogin = jdbcTemplateRepository.findMainLanguageByLogin(login);
        if(mainLanguageByLogin.size() > 0) returnUserRank.setFirstLanguage(mainLanguageByLogin.get(0));
        if(mainLanguageByLogin.size() > 1) returnUserRank.setSecondLanguage(mainLanguageByLogin.get(1));
        if(mainLanguageByLogin.size() > 2) returnUserRank.setThirdLanguage(mainLanguageByLogin.get(2));

//        returnUserRank.setMainLanguage(String.join(",", mainLanguageByLogin));
       return returnUserRank;
    }

    public OrgRankResult orgRankResultMaker(OrgRankTmp orgRankTmp) {
        OrgRankResult returnOrgRank = OrgRankResult.from(orgRankTmp);
        String login = returnOrgRank.getLogin();

        List<String> mainLanguageByLogin = jdbcTemplateRepository.findMainLanguageByLogin(login);
        if(mainLanguageByLogin.size() > 0) returnOrgRank.setFirstLanguage(mainLanguageByLogin.get(0));
        if(mainLanguageByLogin.size() > 1) returnOrgRank.setSecondLanguage(mainLanguageByLogin.get(1));
        if(mainLanguageByLogin.size() > 2) returnOrgRank.setThirdLanguage(mainLanguageByLogin.get(2));

//        returnOrgRank.setMainLanguage(String.join(",", mainLanguageByLogin));
        return returnOrgRank;
    }

}