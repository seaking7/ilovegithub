package com.poc.ilovegithub.core.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.poc.ilovegithub.core.domain.GitRepo;
import com.poc.ilovegithub.core.domain.UserDetail;
import com.poc.ilovegithub.core.domain.UserStatus;
import com.poc.ilovegithub.core.repository.GitRepoRepository;
import com.poc.ilovegithub.dto.GitRepoDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class GitRepoInsertService {

    private final Environment env;
    GitRepoRepository gitRepoRepository;

    public UserDetail gitRepoInsert(UserDetail userDetail) throws InterruptedException {

        UriComponentsBuilder uriBuilder = makeUrlForGetRepo(userDetail);
        UserDetail returnUserDetail = getUserRepoAndSave(userDetail, uriBuilder);
        log.info("UserDetail id : {} login: {}", returnUserDetail.getId(), returnUserDetail.getLogin());
        return returnUserDetail;
    }

    private UserDetail getUserRepoAndSave(UserDetail userDetail, UriComponentsBuilder uriBuilder) throws InterruptedException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        String agentName = env.getProperty("my.git-appName") + "-" + env.getProperty("my.git-login") + "-App";
        headers.set("User-Agent", agentName);
        headers.set("Accept", "application/vnd.github+json");
        headers.set("Authorization", "token "+ env.getProperty("my.git-token"));

        HttpEntity request = new HttpEntity(headers);
        UserDetail returnUserDetail  = userDetail;

        try{
            ResponseEntity<String> response = restTemplate.exchange(
                    uriBuilder.toUriString(),
                    HttpMethod.GET,
                    request,
                    String.class);

            log.debug("StatusCode : {} {}", response.getStatusCode());
            log.debug("Header : {}:", response.getHeaders());

            saveGitRepo(response, userDetail.getLogin(), userDetail.getId());
            returnUserDetail.setStatus(UserStatus.REPO_INSERTED);

        } catch ( HttpClientErrorException e) {
            log.info("Exception  : {} {} {}", userDetail.getLogin(), e.getStatusCode(), e.getMessage());
            if(e.getStatusCode().equals(HttpStatus.NOT_FOUND)){
                returnUserDetail.setStatus(UserStatus.NOT_FOUND);
            } else{
                Thread.sleep(1200000);  //403 API rate limit exceeded. 20분 sleep
            }
        } catch( Exception e ){
            log.info("Exception  : {} {} {}", userDetail.getLogin(), e.getCause(), e.getMessage());
            Thread.sleep(600000);
        }
        return returnUserDetail;
    }

    private UriComponentsBuilder makeUrlForGetRepo(UserDetail userDetail) {
        return UriComponentsBuilder
                .fromUriString("https://api.github.com")
                .path("/users")
                .path("/")
                .path(userDetail.getLogin())
                .path("/repos")
                .encode();
    }

    private void saveGitRepo(ResponseEntity<String> response, String login, Integer userId) {
        Gson gson = new Gson();
        Type collectionType = new TypeToken<List<GitRepoDto>>(){}.getType();
        List<GitRepoDto> gitRepoList  = gson.fromJson( response.getBody() , collectionType);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

        for (GitRepoDto gitRepo : gitRepoList) {
            log.debug(gitRepo.toString());

            GitRepo gitRepository = GitRepo.builder()
                    .login(login)
                    .id(gitRepo.getId())
                    .userId(userId)
                    .name(gitRepo.getName())
                    .size(gitRepo.getSize())
                    .stargazers_count(gitRepo.getStargazers_count())
                    .language(gitRepo.getLanguage())
                    .fetched_on(LocalDateTime.now())
                    .build();
            if(gitRepo.getCreated_at() != null) gitRepository.setCreated_at(LocalDateTime.parse(gitRepo.getCreated_at(), formatter));
            if(gitRepo.getUpdated_at() != null) gitRepository.setUpdated_at(LocalDateTime.parse(gitRepo.getUpdated_at(), formatter));
            if(gitRepo.getPushed_at() != null) gitRepository.setPushed_at(LocalDateTime.parse(gitRepo.getPushed_at(), formatter));

            gitRepoRepository.save(gitRepository);
        }
    }

}
