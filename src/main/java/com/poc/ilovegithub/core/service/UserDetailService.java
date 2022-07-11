package com.poc.ilovegithub.core.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.poc.ilovegithub.core.domain.GithubUser;
import com.poc.ilovegithub.core.domain.UserDetail;
import com.poc.ilovegithub.core.domain.UserStatus;
import com.poc.ilovegithub.core.repository.UserDetailRepository;
import com.poc.ilovegithub.dto.GithubUsersDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    public UserDetail getUserDetail(UserDetail userDetail, String gitToken) throws InterruptedException {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "token "+ gitToken);

        HttpEntity request = new HttpEntity(headers);
        UriComponentsBuilder uriBuilder = UriComponentsBuilder
                .fromUriString("https://api.github.com")
                .path("/users")
                .path("/")
                .path(userDetail.getLogin())
                .encode();


        RestTemplate restTemplate = new RestTemplate();
        UserDetail returnUserDetail  = new UserDetail();

        try{
            ResponseEntity<UserDetail> response = restTemplate.exchange(
                    uriBuilder.toUriString(),
                    HttpMethod.GET,
                    request,
                    UserDetail.class);

            log.debug("StatusCode : {} {}", response.getStatusCode());
            log.debug("Header : {}:", response.getHeaders());

            //Thread.sleep(1000);
            returnUserDetail = response.getBody();
            returnUserDetail.setStatus(UserStatus.DETAIL_UPDATED);
            returnUserDetail.setFetched_on(LocalDateTime.now());
        } catch ( HttpClientErrorException e) {
            log.info("Exception  : {} {} {}", userDetail.getLogin(), e.getStatusCode(), e.getMessage());
            returnUserDetail = userDetail;
            if(e.getStatusCode().equals(HttpStatus.NOT_FOUND)){
                returnUserDetail.setStatus(UserStatus.NOT_FOUND);
            } else{
                Thread.sleep(600000);
            }

        }
        log.info("UserDetail id : {} login: {}", returnUserDetail.getId(), returnUserDetail.getLogin());
        return returnUserDetail;
    }

}
