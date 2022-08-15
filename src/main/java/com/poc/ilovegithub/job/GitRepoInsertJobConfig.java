package com.poc.ilovegithub.job;

import com.poc.ilovegithub.core.domain.UserDetail;
import com.poc.ilovegithub.core.domain.UserStatus;
import com.poc.ilovegithub.core.repository.UserDetailRepository;
import com.poc.ilovegithub.core.service.GitRepoInsertService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Sort;

import java.util.Collections;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class GitRepoInsertJobConfig {
    private final Environment env;
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    private final GitRepoInsertService gitRepoInsertService;
    private final UserDetailRepository userDetailRepository;


    @Bean("GitRepoInsertJob")
    public Job gitRepoInsertJob(Step gitRepoInsertStep) {
        return jobBuilderFactory.get("GitRepoInsertJob")
                .incrementer(new RunIdIncrementer())
                .start(gitRepoInsertStep)
                .build();
    }

    @JobScope
    @Bean("gitRepoInsertStep")
    public Step gitRepoInsertStep(ItemReader gitRepoInsertReader,
                                     ItemProcessor gitRepoInsertProcessor,
                                     ItemWriter gitRepoInsertWriter) {
        return stepBuilderFactory.get("gitRepoInsertStep")
                .<UserDetail, UserDetail>chunk(Integer.parseInt(env.getProperty("my.fetch-count")))
                .reader(gitRepoInsertReader)
                .processor(gitRepoInsertProcessor)
                .writer(gitRepoInsertWriter)
                .build();
    }

    @StepScope
    @Bean
    public RepositoryItemReader<UserDetail> gitRepoInsertReader() {
        return new RepositoryItemReaderBuilder<UserDetail>()
                .name("gitRepoInsertReader")
                .repository(userDetailRepository)
                .methodName("findByStatusEqualsAndIdGreaterThanAndIdLessThan")
                .pageSize(Integer.parseInt(env.getProperty("my.fetch-count")))
                .arguments(UserStatus.DETAIL_UPDATED, Integer.parseInt(env.getProperty("my.start-from-id")), Integer.parseInt(env.getProperty("my.end_to_id")))
                .sorts(Collections.singletonMap("id", Sort.Direction.ASC))
                .build();
    }

    @StepScope
    @Bean
    public ItemProcessor<UserDetail, UserDetail> gitRepoInsertProcessor() {
        return gitRepoInsertService::gitRepoInsert;
    }

    @StepScope
    @Bean
    public ItemWriter<UserDetail> gitRepoInsertWriter() {
        return items -> items.forEach(userDetailRepository::save);
    }
}
