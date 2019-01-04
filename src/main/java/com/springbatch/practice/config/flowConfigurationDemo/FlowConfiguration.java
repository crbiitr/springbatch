package com.springbatch.practice.config.flowConfigurationDemo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
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
public class FlowConfiguration {

    public final Logger logger = LoggerFactory.getLogger(FlowConfiguration.class);
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .tasklet(((stepContribution, chunkContext) -> {
                    logger.info("================================>> Inside FlowConfiguration::step1 method, Running step1");
                    return RepeatStatus.FINISHED;
                })).build();
    }

    @Bean
    public Step step2() {
        return stepBuilderFactory.get("step2")
                .tasklet(((stepContribution, chunkContext) -> {
                    logger.info("================================>> Inside FlowConfiguration::step2 method, Running step2");
                    return RepeatStatus.FINISHED;
                })).build();
    }

    @Bean
    public Flow testFlow() {
        FlowBuilder<Flow> flowBuilder = new FlowBuilder<>("testFlow");
        logger.info("#################################->> Inside FlowConfiguration::testFlow method, Starting testFlow");
        flowBuilder.start(step1())
                .next(step2())
                .end();
        logger.info("#################################->> Inside FlowConfiguration::testFlow method, Finished testFlow");
        return flowBuilder.build();
    }

}
