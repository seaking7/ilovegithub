package com.poc.ilovegithub.job;

import com.poc.ilovegithub.core.service.GitUserService;
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


@Slf4j
@Configuration
@RequiredArgsConstructor
public class GithubUsersJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    private final GitUserService gitUserService;


    @Bean("GithubUsersJob")
    public Job githubUsersJob(Step githubUsersStep) {
        return jobBuilderFactory.get("GithubUsersJob")
                .incrementer(new RunIdIncrementer())
                .start(githubUsersStep)
                .build();
    }

    @JobScope
    @Bean("githubUsersStep")
    public Step githubUsersStep(Tasklet githubUsersTasklet) {
        return stepBuilderFactory.get("githubUsersStep")
                .tasklet(githubUsersTasklet)
                .build();
    }

    @StepScope
    @Bean
    public Tasklet githubUsersTasklet(@Value("#{jobParameters['loopCount']}") String loopCount) {
        return (contribution, chunkContext) -> {

            gitUserService.insertUserListFromGithubForLoop(loopCount);

            return RepeatStatus.FINISHED;
        };
    }



}
