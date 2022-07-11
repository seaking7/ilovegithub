package com.poc.ilovegithub.core.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.poc.ilovegithub.core.domain.GithubUser;
import com.poc.ilovegithub.core.repository.GithubUserRepository;
import com.poc.ilovegithub.dto.GithubUsersDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.lang.reflect.Type;
import java.util.List;

import static java.lang.Thread.sleep;

@Slf4j
@Service
@AllArgsConstructor
public class GitUserService {

    public final static int READ_PER_PAGE = 100;
    private final GithubUserRepository githubUserRepository;


    public void insertUserListFromGithubForLoop(String loopCount, String gitToken) throws InterruptedException {

        String currentMaxGithubId = githubUserRepository.findMaxGitId();
        log.info("Start githubUsersTasklet Batch start from {}", currentMaxGithubId);

        int start = Integer.valueOf(currentMaxGithubId);
        int loop_cnt = Integer.valueOf(loopCount);

        for(int i = 1; i <= loop_cnt; i++){
            insertUserListFromGithub(start, READ_PER_PAGE, gitToken);
            start += READ_PER_PAGE;
        }
    }

    @Transactional
    public void insertUserListFromGithub(int since, int per_page, String gitToken) throws InterruptedException {
        UriComponentsBuilder uriBuilder = makeUrlForGetUser(since, per_page);
        getUserListAndSave(since, gitToken, uriBuilder);
    }

    private UriComponentsBuilder makeUrlForGetUser(int since, int per_page) {
        return UriComponentsBuilder
                .fromUriString("https://api.github.com")
                .path("/users")
                .queryParam("since", since)
                .queryParam("per_page", per_page)
                .encode();
    }

    private void getUserListAndSave(int since, String gitToken, UriComponentsBuilder uriBuilder) throws InterruptedException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "token "+ gitToken);

        HttpEntity request = new HttpEntity(headers);
        try{
            ResponseEntity<String> response = restTemplate.exchange(
                    uriBuilder.toUriString(),
                    HttpMethod.GET,
                    request,
                    String.class);

            log.info("StatusCode : {} {}", since, response.getStatusCode());
            log.debug("Header : {}:", response.getHeaders());
            log.debug("Body : {}", response.getBody());

            saveGitUser(response);
        } catch ( HttpClientErrorException e) {
            log.info("sleep 600s : {} {}", e.getStatusCode(), e.getMessage());
            Thread.sleep(600000);
        }
    }

    private void saveGitUser(ResponseEntity<String> response) {
        Gson gson = new Gson();
        Type collectionType = new TypeToken<List<GithubUsersDto>>(){}.getType();
        List<GithubUsersDto> githubUsersDtoList  = gson.fromJson( response.getBody() , collectionType);

        for (GithubUsersDto githubUsersDto : githubUsersDtoList) {
            log.debug(githubUsersDto.toString());
            GithubUser githubUser = GithubUser.builder()
                    .login(githubUsersDto.getLogin())
                    .git_id(githubUsersDto.getId())
                    .type(githubUsersDto.getType())
                    .build();
            githubUserRepository.save(githubUser);
        }
        githubUserRepository.flush();
    }


}
