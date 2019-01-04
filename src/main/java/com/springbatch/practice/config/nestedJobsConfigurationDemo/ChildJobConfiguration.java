package com.springbatch.practice.config.nestedJobsConfigurationDemo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Chetan Raj
 * @implNote
 * @since : 03/01/19
 */
@Configuration
public class ChildJobConfiguration {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    public final static Logger logger = LoggerFactory.getLogger(ChildJobConfiguration.class);

    @Bean
    public Step childParentStep1a() {
        return stepBuilderFactory.get("childParentStep1a")
                .tasklet(((stepContribution, chunkContext) -> {
                    logger.info("================================>> Inside ChildJobConfiguration::childParentStep1a method, This is the childParentStep1a tasklet");
                    return RepeatStatus.FINISHED;
                })).build();
    }

    @Bean
    public Job childJob() {
        logger.info("***********************************->> Inside ChildJobConfiguration::childJob method, Executing childJob");
        return jobBuilderFactory.get("childJob")
                .start(childParentStep1a())
                .build();
    }

}
