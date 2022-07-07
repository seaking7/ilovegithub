//package com.poc.ilovegithub.job;
//
//import com.poc.ilovegithub.core.repository.GithubUserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.batch.core.Job;
//import org.springframework.batch.core.Step;
//import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
//import org.springframework.batch.core.configuration.annotation.JobScope;
//import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
//import org.springframework.batch.core.configuration.annotation.StepScope;
//import org.springframework.batch.core.launch.support.RunIdIncrementer;
//import org.springframework.batch.item.ItemProcessor;
//import org.springframework.batch.item.ItemReader;
//import org.springframework.batch.item.ItemWriter;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.domain.Sort;
//
//import java.util.Collections;
//import java.util.List;
//
//@Configuration
//@RequiredArgsConstructor
//public class GithubUserInsertJobConfig {
//
//    private final JobBuilderFactory jobBuilderFactory;
//    private final StepBuilderFactory stepBuilderFactory;
//
//    private final GithubUserRepository githubUserRepository;
//
//    @Bean("githubUserInsertJob")
//    public Job githubUserInsertJob(Step githubUserInsertStep) {
//        return jobBuilderFactory.get("githubUserInsertJob")
//                .incrementer(new RunIdIncrementer())
//                .start(githubUserInsertStep)
//                .build();
//    }
//
//    @JobScope
//    @Bean("plainTextStep")
//    public Step githubUserInsertStep(ItemReader plainTextReader,
//                                     ItemProcessor plainTextProcessor,
//                                     ItemWriter plainTextWriter) {
//        return stepBuilderFactory.get("plainTextStep")
//                .<PlainText, String>chunk(5)
//                .reader(plainTextReader)
//                .processor(plainTextProcessor)
//                .writer(plainTextWriter)
//                .build();
//    }
//
//    @StepScope
//    @Bean
//    public RepositoryItemReader<PlainText> githubUserFileReader() {
//        return new RepositoryItemReaderBuilder<PlainText>()
//                .name("plainTextReader")
//                .repository(plainTextRepository)
//                .methodName("findBy")
//                .pageSize(5)
//                .arguments(List.of())
//                .sorts(Collections.singletonMap("id", Sort.Direction.DESC))
//                .build();
//    }
//
//    @StepScope
//    @Bean
//    public ItemProcessor<PlainText, String> plainTextProcessor() {
//        return item -> "processed " + item.getText();
//    }
//
//    @StepScope
//    @Bean
//    public ItemWriter<String> plainTextWriter() {
//        return items -> {
//            items.forEach(item -> resultTextRepository.save(new ResultText(null, item)));
//            System.out.println("==== chunk is finished");
//        };
//    }
//}
