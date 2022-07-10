package com.poc.ilovegithub.job;

import com.poc.ilovegithub.core.domain.GithubUser;
import com.poc.ilovegithub.core.repository.GithubUserRepository;
import com.poc.ilovegithub.core.service.GitUserService;
import lombok.RequiredArgsConstructor;
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
import org.springframework.data.domain.Sort;

import java.util.Collections;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class GithubUserInsertJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    private final GitUserService gitUserService;
    private final GithubUserRepository githubUserRepository;

    @Bean("githubUserInsertJob")
    public Job githubUserInsertJob(Step githubUserInsertStep) {
        return jobBuilderFactory.get("githubUserInsertJob")
                .incrementer(new RunIdIncrementer())
                .start(githubUserInsertStep)
                .build();
    }

    @JobScope
    @Bean("githubUserInsertStep")
    public Step githubUserInsertStep(ItemReader githubUserReader,
                                     ItemProcessor githubUserProcessor,
                                     ItemWriter githubUserWriter) {
        return stepBuilderFactory.get("githubUserInsertStep")
                .<GithubUser, String>chunk(5)
                .reader(githubUserReader)
                .processor(githubUserProcessor)
                .writer(githubUserWriter)
                .build();
    }

    @StepScope
    @Bean
    public RepositoryItemReader<GithubUser> githubUserReader() {
        return new RepositoryItemReaderBuilder<GithubUser>()
                .name("githubUserReader")
                .repository(githubUserRepository)
                .methodName("findBy")
                .pageSize(5)
                .arguments(List.of())
                .sorts(Collections.singletonMap("id", Sort.Direction.DESC))
                .build();
    }

    @StepScope
    @Bean
    public ItemProcessor<GithubUser, String> githubUserProcessor() {
        return new ItemProcessor<GithubUser, String>() {
            @Override
            public String process(GithubUser item) throws Exception {
                gitUserService.getUserDetail(item.getLogin());
                return "processed " + item.getLogin();
            }
        };
    }

    @StepScope
    @Bean
    public ItemWriter<String> githubUserWriter() {
        return items -> {
           // items.forEach(item -> githubUserRepository.save(new ResultText(null, item)));
            System.out.println("==== chunk is finished"+items.toString());
        };
    }
}
