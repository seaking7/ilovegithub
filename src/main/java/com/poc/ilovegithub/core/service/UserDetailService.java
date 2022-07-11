package com.poc.ilovegithub.core.service;

import com.poc.ilovegithub.core.domain.UserDetail;
import com.poc.ilovegithub.core.domain.UserStatus;
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

    public UserDetail updateUserDetailInfo(UserDetail userDetail, String gitToken) throws InterruptedException {

        UriComponentsBuilder uriBuilder = makeUrlForGetUserDetail(userDetail);
        UserDetail returnUserDetail = getUserDetail(userDetail, gitToken, uriBuilder);
        log.info("UserDetail id : {} login: {}", returnUserDetail.getId(), returnUserDetail.getLogin());
        return returnUserDetail;
    }

    private UserDetail getUserDetail(UserDetail userDetail, String gitToken, UriComponentsBuilder uriBuilder) throws InterruptedException {
        UserDetail returnUserDetail;

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "token "+ gitToken);

        HttpEntity request = new HttpEntity(headers);
        try{
            ResponseEntity<UserDetail> response = restTemplate.exchange(
                    uriBuilder.toUriString(),
                    HttpMethod.GET,
                    request,
                    UserDetail.class);

            log.debug("StatusCode : {} {}", response.getStatusCode());
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
                Thread.sleep(600000);       //403 API rate limit exceeded. 10분 sleep
            }

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
