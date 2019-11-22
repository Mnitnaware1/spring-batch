package com.mastercard.mcbs.pbrc.offline.batch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.mastercard.mcbs.pbrc.offline.batch.model.JobParamModel;

@SpringBootApplication
@EnableBatchProcessing
@EnableScheduling
public class FeederBatchApplication {

	/**
	 * This is main application class.
	 *
	 * @param args .
	 */
	public static void main(String[] args) {
		SpringApplication.run(FeederBatchApplication.class, args);
	}

	@Bean
	public JobParamModel jobParamModel() {
		return new JobParamModel();
	}

}
