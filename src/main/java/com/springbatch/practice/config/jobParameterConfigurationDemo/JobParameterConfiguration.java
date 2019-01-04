package com.springbatch.practice.config.jobParameterConfigurationDemo;

import com.springbatch.practice.config.batchConfigurationDemo.BatchConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Chetan Raj
 * @implNote Command to run this program: 'java -jar target/spring-batch-0.0.1-SNAPSHOT.jar message=Hello!'
 * @since : 04/01/19
 */
@Configuration
public class JobParameterConfiguration {
    public final Logger logger = LoggerFactory.getLogger(JobParameterConfiguration.class);

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    @StepScope
    public Tasklet jobParameterTasklet(@Value("#{jobParameters['message']}") String message) {
        logger.info("#################################->> Executing JobParameterConfiguration::jobParameterTasklet.");
        return ((stepContribution, chunkContext) -> {
            logger.info("Message :: ", message);
            return RepeatStatus.FINISHED;
        });
    }

    @Bean
    public Step jobParameterStep1() {
        logger.info("=================================->> Executing JobParameterConfiguration::jobParameterStep1.");
        return stepBuilderFactory.get("jobParameterStep1")
                .tasklet(jobParameterTasklet(null))
                .build();
    }

    @Bean
    public Job myParameterJob() {
        logger.info("***********************************->> Inside JobParameterConfiguration::myParameterJob method, Executing myParameterJob");
        return jobBuilderFactory.get("myParameterJob")
                .start(jobParameterStep1())
                .build();
    }

}
