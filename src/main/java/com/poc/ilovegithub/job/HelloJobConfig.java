package com.poc.ilovegithub.job;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.poc.ilovegithub.core.domain.GithubUser;
import com.poc.ilovegithub.core.repository.GithubUserRepository;
import com.poc.ilovegithub.dto.GithubUsersDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.lang.reflect.Type;
import java.net.URI;
import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class HelloJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    private final GithubUserRepository githubUserRepository;

    @Bean("hellJob")
    public Job helloJob(Step helloStep) {
        return jobBuilderFactory.get("helloJob")
                .incrementer(new RunIdIncrementer())
                .start(helloStep)
                .build();
    }

    @JobScope
    @Bean("helloStep")
    public Step helloStep(Tasklet tasklet) {
        return stepBuilderFactory.get("helloStep")
                .tasklet(tasklet)
                .build();
    }

    @StepScope
    @Bean
    public Tasklet tasklet(@Value("#{jobParameters['startValue']}") String startValue,
                           @Value("#{jobParameters['loopCount']}") String loopCount) {
        return (contribution, chunkContext) -> {
            System.out.println("Hello Spring Batch");

            int start = Integer.valueOf(startValue);
            int loop_cnt = Integer.valueOf(loopCount);


            for(int i = 1; i <= loop_cnt; i++){
                readUserFromGithub(start, 100);
                start += 100;
            }

            return RepeatStatus.FINISHED;
        };
    }

    private void readUserFromGithub(int since, int per_page) {


        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "token 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");

        HttpEntity request = new HttpEntity(headers);
//adding the query params to the URL
        UriComponentsBuilder uriBuilder = UriComponentsBuilder
                .fromUriString("https://api.github.com")
                .path("/users")
                .queryParam("since",since)
                .queryParam("per_page", per_page)
                .encode();


        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                uriBuilder.toUriString(),
                HttpMethod.GET,
                request,
                String.class
        );

        log.info("StatusCode : {}", response.getStatusCode());
        log.info("Header : {}:", response.getHeaders());
        log.info("Body : {}", response.getBody());

        Gson gson = new Gson();
        Type collectionType = new TypeToken<List<GithubUsersDto>>(){}.getType();
        List<GithubUsersDto> githubUsersDtoList  = gson.fromJson( response.getBody() , collectionType);

        for (GithubUsersDto githubUsersDto : githubUsersDtoList) {
            log.info(githubUsersDto.toString());
            GithubUser githubUser = GithubUser.builder()
                    .login(githubUsersDto.getLogin())
                    .git_id(githubUsersDto.getId())
                    .type(githubUsersDto.getType())
                    .build();
            githubUserRepository.save(githubUser);
        }

//        log.info(userSites.toString());
    }

    private void readUserFromGithub3(int since, int per_page) {
        URI uri = UriComponentsBuilder
                .fromUriString("https://api.github.com")
                .path("/users")
                .queryParam("since",since)
                .queryParam("per_page", per_page)
                .encode()
                .build()
                .toUri();
        log.info("uri : {}",uri);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);

        log.info("StatusCode : {}", response.getStatusCode());
        log.info("Header : {}:", response.getHeaders());
        log.info("Body : {}", response.getBody());

        Gson gson = new Gson();
        Type collectionType = new TypeToken<List<GithubUsersDto>>(){}.getType();
        List<GithubUsersDto> githubUsersDtoList  = gson.fromJson( response.getBody() , collectionType);

        for (GithubUsersDto githubUsersDto : githubUsersDtoList) {
            log.info(githubUsersDto.toString());
            GithubUser githubUser = GithubUser.builder()
                    .login(githubUsersDto.getLogin())
                    .git_id(githubUsersDto.getId())
                    .type(githubUsersDto.getType())
                    .build();
            githubUserRepository.save(githubUser);
        }

//        log.info(userSites.toString());
    }

    private void readUserFromGithub2(int since, int per_page) {
        URI uri = UriComponentsBuilder
                .fromUriString("https://api.github.com")
                .path("/users")
                .queryParam("since",since)
                .queryParam("per_page",since)
                .encode()
                .build()
                .toUri();
        log.info("uri : {}",uri);

        RestTemplate restTemplate = new RestTemplate();
        String user = restTemplate.getForObject(uri, String.class);
        log.info("user : {}", user);
    }
}
