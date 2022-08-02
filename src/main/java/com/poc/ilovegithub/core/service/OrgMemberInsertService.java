package com.poc.ilovegithub.core.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.poc.ilovegithub.core.domain.OrgMembers;
import com.poc.ilovegithub.core.domain.UserDetail;
import com.poc.ilovegithub.core.domain.UserStatus;
import com.poc.ilovegithub.core.repository.OrgMembersRepository;
import com.poc.ilovegithub.dto.OrgMembersDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.lang.reflect.Type;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class OrgMemberInsertService {

    private final Environment env;
    public final static int MAX_ORG_MEMBER_PAGE = 100;
    OrgMembersRepository orgMembersRepository;

    public UserDetail orgMemberInsert(UserDetail userDetail) throws InterruptedException {

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        String agentName = env.getProperty("my.git-appName") + "-" + env.getProperty("my.git-login") + "-App";
        headers.set("User-Agent", agentName);
        headers.set("Accept", "application/vnd.github+json");
        headers.set("Authorization", "token "+ env.getProperty("my.git-token"));

        HttpEntity request = new HttpEntity(headers);
        UserDetail returnUserDetail1 = userDetail;

        for(int i=1; i< MAX_ORG_MEMBER_PAGE; i++)
        {
            UriComponentsBuilder uriBuilder = UriComponentsBuilder
                    .fromUriString("https://api.github.com")
                    .path("/orgs")
                    .path("/")
                    .path(userDetail.getLogin())
                    .path("/members")
                    .queryParam("page", i)
                    .queryParam("per_page", 100)
                    .encode();

            try{

                ResponseEntity<String> response = restTemplate.exchange(
                        uriBuilder.toUriString(),
                        HttpMethod.GET,
                        request,
                        String.class);

                log.debug("StatusCode : {} {}", response.getStatusCode());
                log.debug("Header : {}:", response.getHeaders());
                log.debug("Body : {}:", response.getBody());

                if(response.getBody().toString().equals("[]")){
                    returnUserDetail1.setStatus(UserStatus.ORG_INSERTED);
                    break;
                }

                saveOrgMembers(response, userDetail.getLogin(), userDetail.getId());
                returnUserDetail1.setStatus(UserStatus.ORG_INSERTED);

            } catch ( HttpClientErrorException e) {
                log.info("Exception  : {} {} {}", userDetail.getLogin(), e.getStatusCode(), e.getMessage());
                if(e.getStatusCode().equals(HttpStatus.NOT_FOUND)){
                    returnUserDetail1.setStatus(UserStatus.NOT_FOUND);
                    break;
                } else{
                    Thread.sleep(1200000);  //403 API rate limit exceeded. 20분 sleep
                    i--;      //403인 경우 해당 page 다시 시도함
                }
            } catch( Exception e ){
                log.info("Exception  : {} {} {}", userDetail.getLogin(), e.getCause(), e.getMessage());
                Thread.sleep(600000);
                i--;
            }
        }

        UserDetail returnUserDetail = returnUserDetail1;

        log.info("UserDetail id : {} login: {}", returnUserDetail.getId(), returnUserDetail.getLogin());
        return returnUserDetail;
    }

    private void saveOrgMembers(ResponseEntity<String> response, String orgLogin, Integer orgId) {
        Gson gson = new Gson();
        Type collectionType = new TypeToken<List<OrgMembersDto>>(){}.getType();
        List<OrgMembersDto> orgMemberList  = gson.fromJson( response.getBody() , collectionType);

        for (OrgMembersDto orgMembersDto : orgMemberList) {
            log.debug(orgMembersDto.toString());

            OrgMembers orgMember = OrgMembers.builder()
                    .memberId(orgMembersDto.getId())
                    .memberLogin(orgMembersDto.getLogin())
                    .orgId(orgId)
                    .orgLogin(orgLogin)
                    .type(orgMembersDto.getType())
                    .build();

            orgMembersRepository.save(orgMember);
        }
    }

}
