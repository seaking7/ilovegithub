package com.poc.ilovegithub.core.service;

import com.poc.ilovegithub.core.domain.UserDetail;
import com.poc.ilovegithub.core.domain.UserStatus;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;

@Slf4j
@Service
@AllArgsConstructor
public class UserDetailService {
    private final Environment env;


    public UserDetail updateUserDetailInfo(UserDetail userDetail) throws InterruptedException {

        UriComponentsBuilder uriBuilder = makeUrlForGetUserDetail(userDetail);
        UserDetail returnUserDetail = getUserDetail(userDetail, uriBuilder);
        log.info("UserDetail id : {} login: {}", returnUserDetail.getId(), returnUserDetail.getLogin());
        return returnUserDetail;
    }

    private UserDetail getUserDetail(UserDetail userDetail, UriComponentsBuilder uriBuilder) throws InterruptedException {
        UserDetail returnUserDetail;

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        String agentName = env.getProperty("my.git-appName") + "-" + env.getProperty("my.git-login") + "-App";
        headers.set("User-Agent", agentName);
        headers.set("Accept", "application/vnd.github+json");
        headers.set("Authorization", "token "+ env.getProperty("my.git-token"));

        HttpEntity request = new HttpEntity(headers);
        try{
            ResponseEntity<UserDetail> response = restTemplate.exchange(
                    uriBuilder.toUriString(),
                    HttpMethod.GET,
                    request,
                    UserDetail.class);

            log.debug("StatusCode : {}", response.getStatusCode());
            log.debug("Header : {}:", response.getHeaders());

            returnUserDetail = response.getBody();
            returnUserDetail.setStatus(UserStatus.DETAIL_UPDATED);
            returnUserDetail.setFetched_on(LocalDateTime.now());
        } catch ( HttpClientErrorException e) {
            log.info("Exception  : {} {} {}", userDetail.getLogin(), e.getStatusCode(), e.getMessage());
            returnUserDetail = userDetail;

            if(e.getStatusCode().equals(HttpStatus.NOT_FOUND)){
                returnUserDetail.setStatus(UserStatus.NOT_FOUND);
            } else{
                Thread.sleep(1200000);       //403 API rate limit exceeded. 20분 sleep
            }
        } catch( Exception e ){
            log.info("Exception  : {} {} {}", userDetail.getLogin(), e.getCause(), e.getMessage());
            returnUserDetail = userDetail;
            Thread.sleep(600000);
        }

        return returnUserDetail;
    }

    private UriComponentsBuilder makeUrlForGetUserDetail(UserDetail userDetail) {
        return UriComponentsBuilder
                .fromUriString("https://api.github.com")
                .path("/users")
                .path("/")
                .path(userDetail.getLogin())
                .encode();
    }

}
