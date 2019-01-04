package com.springbatch.practice.config.flowConfigurationDemo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Chetan Raj
 * @implNote
 * @since : 02/01/19
 */
@Configuration
public class FlowFirstConfiguration {
    public final Logger logger = LoggerFactory.getLogger(FlowFirstConfiguration.class);
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Step myStep() {
        return stepBuilderFactory.get("myStep")
                .tasklet(((stepContribution, chunkContext) -> {
                    logger.info("================================>> Inside FlowFirstConfiguration::myStep method, Executed myStep");
                    return RepeatStatus.FINISHED;
                })).build();
    }

    @Bean
    public Job flowFirstJob(Flow testFlow) {
        logger.info("***********************************->> Inside FlowFirstConfiguration::flowFirstJob method, Executing flowFirstJob");
        return jobBuilderFactory.get("flowFirstJob")
                .start(testFlow)
                .next(myStep())
                .end()
                .build();
    }
}
