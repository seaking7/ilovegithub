package com.poc.ilovegithub.job.rank;

import com.poc.ilovegithub.core.domain.rank.RankJobIndex;
import com.poc.ilovegithub.core.domain.UserDetail;
import com.poc.ilovegithub.core.repository.JdbcTemplateRepository;
import com.poc.ilovegithub.core.repository.rank.RankJobIndexRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;

import java.util.Collections;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class RankUpdateJobConfig {

    private final static int BATCH_SIZE_RANK_UPDATE = 2;
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final RankJobIndexRepository rankJobIndexRepository;

    private final JdbcTemplateRepository jdbcTemplateRepository;


    @Bean("RankUpdateJob")
    public Job rankUpdateJob(Step userRankStep,
                             Step userRankMainLanguageStep,
                             Step orgRankStep,
                             Step orgRankMainLanguageStep) {
        return jobBuilderFactory.get("RankUpdateJob")
                .incrementer(new RunIdIncrementer())
                .start(userRankStep)
                .next(userRankMainLanguageStep)
                .next(orgRankStep)
                .next(orgRankMainLanguageStep)
                .build();
    }

    @JobScope
    @Bean("userRankStep")
    public Step userRankStep(ItemReader userRankReader,
                             ItemWriter userRankWriter) {
        return stepBuilderFactory.get("userRankStep")
                .listener(userRankStepListener())
                .<UserDetail, UserDetail>chunk(BATCH_SIZE_RANK_UPDATE)
                .reader(userRankReader)
                .writer(userRankWriter)
                .build();
    }

    public StepExecutionListener userRankStepListener(){
        return new StepExecutionListener() {
            @Override
            public void beforeStep(StepExecution stepExecution) {
                log.info("===userRankStep START, updateKoreanInfo start");
                jdbcTemplateRepository.updateKoreanInfo();
                log.info("===userRankStep ING, updateSourceRank start");
                jdbcTemplateRepository.updateSourceRankResult();
                log.info("---userRankStep ING,  updateKoreanInfo End, userRankStep truncate start");
                jdbcTemplateRepository.truncateUserLankTmp();
            }

            @Override
            public ExitStatus afterStep(StepExecution stepExecution) {
                log.info("==== userRankStep END");
                return null;
            }
        };
    }

    @StepScope
    @Bean
    public RepositoryItemReader<RankJobIndex> userRankReader() {
        return new RepositoryItemReaderBuilder<RankJobIndex>()
                .name("userRankReader")
                .repository(rankJobIndexRepository)
                .methodName("findByRankTableEquals")
                .pageSize(BATCH_SIZE_RANK_UPDATE)
                .arguments("g_user_rank")
                .sorts(Collections.singletonMap("id", Sort.Direction.ASC))
                .build();
    }


    @StepScope
    @Bean
    public ItemWriter<RankJobIndex> userRankWriter() {
        return items -> items.forEach(item -> jdbcTemplateRepository.insertUserRankTmp(item.getFromId(), item.getToId()));
    }



    @JobScope
    @Bean("orgRankStep")
    public Step orgRankStep(ItemReader orgRankReader,
                             ItemWriter orgRankWriter) {
        return stepBuilderFactory.get("orgRankStep")
                .listener(orgRankStepListener())
                .<UserDetail, UserDetail>chunk(BATCH_SIZE_RANK_UPDATE)
                .reader(orgRankReader)
                .writer(orgRankWriter)
                .build();
    }

    public StepExecutionListener orgRankStepListener(){
        return new StepExecutionListener() {
            @Override
            public void beforeStep(StepExecution stepExecution) {
                log.info("===orgRankStep START ,  truncateOrgLankTmp start");
                jdbcTemplateRepository.truncateOrgLankTmp();
            }

            @Override
            public ExitStatus afterStep(StepExecution stepExecution) {
                log.info("===orgRankStep END");
                return null;
            }
        };
    }

    @StepScope
    @Bean
    public RepositoryItemReader<RankJobIndex> orgRankReader() {
        return new RepositoryItemReaderBuilder<RankJobIndex>()
                .name("orgRankReader")
                .repository(rankJobIndexRepository)
                .methodName("findByRankTableEquals")
                .pageSize(BATCH_SIZE_RANK_UPDATE)
                .arguments("g_org_rank")
                .sorts(Collections.singletonMap("id", Sort.Direction.ASC))
                .build();
    }


    @StepScope
    @Bean
    public ItemWriter<RankJobIndex> orgRankWriter() {
        return items -> items.forEach(item -> jdbcTemplateRepository.insertOrgRankTmp(item.getFromId(), item.getToId()));
    }



}
