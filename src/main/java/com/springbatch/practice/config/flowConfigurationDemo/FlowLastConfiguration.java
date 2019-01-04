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
public class FlowLastConfiguration {
    public final Logger logger = LoggerFactory.getLogger(FlowFirstConfiguration.class);
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Step myStep() {
        return stepBuilderFactory.get("myStep")
                .tasklet(((stepContribution, chunkContext) -> {
                    logger.info("================================>> Inside FlowLastConfiguration::myStep method, Executed myStep");
                    return RepeatStatus.FINISHED;
                })).build();
    }

    @Bean
    public Job flowLastJob(Flow testFlow) {
        logger.info("***********************************->> Inside FlowLastConfiguration::flowLastJob method, Executing flowLastJob");
        return jobBuilderFactory.get("flowLastJob")
                .start(myStep())
                .on("COMPLETED").to(testFlow)
                .end()
                .build();
    }
}
