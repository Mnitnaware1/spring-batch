package com.mastercard.mcbs.pbrc.offline.batch.config;

import static com.mastercard.mcbs.pbrc.offline.batch.constant.ApplicationConstant.FEEDER_BATCH_JOB;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.FlatFileHeaderCallback;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

import com.mastercard.mcbs.pbrc.offline.batch.listener.FeederJobExecutionListener;
import com.mastercard.mcbs.pbrc.offline.batch.listener.FileVerificationSkipper;
import com.mastercard.mcbs.pbrc.offline.batch.listener.StepExecutionListener;
import com.mastercard.mcbs.pbrc.offline.batch.model.JobParamModel;
import com.mastercard.mcbs.pbrc.offline.batch.model.OfflineRowMapper;
import com.mastercard.mcbs.pbrc.offline.batch.processor.HeadersProcessor;
import com.mastercard.mcbs.pbrc.offline.batch.util.StringUtil;

@Component
@Configuration
@ConfigurationProperties(prefix = "batch.input")
public class OfflineBatchConfiguration extends JobExecutionListenerSupport {

	@Autowired
	private final DataSource dataSource;
	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;
	private final FeederJobExecutionListener feederJobExecutionListener;

	@Autowired
	private JobParamModel jobParamModel;
	private String chunkSize;
	private String maxThread;

	/**
	 * This is a constructor .
	 *
	 * @param dataSource         .
	 * @param jobBuilderFactory  .
	 * @param stepBuilderFactory .
	 * @param sequenceService    .
	 */
	public OfflineBatchConfiguration(DataSource dataSource, JobBuilderFactory jobBuilderFactory,
			StepBuilderFactory stepBuilderFactory, FeederJobExecutionListener feederJobExecutionListener) {
		this.dataSource = dataSource;
		this.jobBuilderFactory = jobBuilderFactory;
		this.stepBuilderFactory = stepBuilderFactory;
		this.feederJobExecutionListener = feederJobExecutionListener;
	}

	@Bean
	@StepScope
	public JdbcCursorItemReader<Map<String, Object>> reader() {
		JdbcCursorItemReader<Map<String, Object>> reader = new JdbcCursorItemReader<>();
		reader.setDataSource(dataSource);
		reader.setSql(jobParamModel.getQueryBuilder());
		reader.setRowMapper(new OfflineRowMapper());
		return reader;
	}

//	@Bean
//	@StepScope
//old

	/*
	 * public FlatFileItemWriter<Map<String, Object>> writer() {
	 * FlatFileItemWriter<Map<String, Object>> writer = new FlatFileItemWriter<>();
	 * writer.setResource(new FileSystemResource("D:\\user.csv")); // write the
	 * headers to csv file writer.setHeaderCallback(new FlatFileHeaderCallback() {
	 * public void writeHeader(Writer writer) throws IOException {
	 * writer.write(StringUtil.convertArrayToString(jobParamModel.getHeaders())); }
	 * });
	 * 
	 * writer.setLineAggregator(new DelimitedLineAggregator<Map<String, Object>>() {
	 * { setDelimiter(","); setFieldExtractor(new
	 * BeanWrapperFieldExtractor<Map<String, Object>>() { {
	 * setNames(jobParamModel.getHeaders()); } }); } }); return writer; }
	 */
	@Bean
	public ItemWriter<Map<String, Object>> writer() {
		return new FlatFileItemWriterBuilder<Map<String, Object>>()

				.name("file-writer").resource(new FileSystemResource("D:\\offline_request.csv"))

				.headerCallback(new FlatFileHeaderCallback() {
					public void writeHeader(Writer writer) throws IOException {
						writer.write(StringUtil.convertArrayToString(jobParamModel.getHeaders()));
					}
				})

				.lineAggregator(new DelimitedLineAggregator<Map<String, Object>>() {
					{
						setDelimiter(",");
						setFieldExtractor(offlineRequestData -> {
							return offlineRequestData.values().toArray(new Object[0]);
						});
					};
				}).build();
	}

	/**
	 * This is a feederBatchJob method .
	 *
	 * @param feederStep .
	 * @return Job .
	 */
	@Bean(name = "feederBatchJob")
	public Job feederBatchJob(Step feederStep) {
		return jobBuilderFactory.get(FEEDER_BATCH_JOB).incrementer(new RunIdIncrementer()).start(feederStep)
				.listener(feederJobExecutionListener).build();
	}

	/**
	 * This is a taskExecutor method.
	 *
	 * @return TaskExecutor .
	 */
	@Bean
	public TaskExecutor taskExecutor() {
		SimpleAsyncTaskExecutor taskExecutor = new SimpleAsyncTaskExecutor();
		taskExecutor.setConcurrencyLimit(Integer.parseInt(maxThread));
		return taskExecutor;
	}

	@Bean
	public SkipPolicy fileVerificationSkipper() {
		return new FileVerificationSkipper();
	}

	@Bean
	@StepScope
	public StepExecutionListener stepExecutionListener() {
		return new StepExecutionListener(jobParamModel.getFeederBatchAudit().getFileLocation());
	}

	/**
	 * This is a feederStep method .
	 *
	 * @param <T>                   .
	 * @param reader                .
	 * @param writer                .
	 * @param stepExecutionListener .
	 * @return Step .
	 */
	/*
	 * @SuppressWarnings("unchecked")
	 * 
	 * @Bean public <TempHeaders> Step feederStep(ItemReader<TempHeaders> reader,
	 * ItemWriter<TempHeaders> writer, StepExecutionListener stepExecutionListener)
	 * { return stepBuilderFactory.get(FEEDER_STEP) .<TempHeaders,
	 * TempHeaders>chunk(Integer.parseInt(chunkSize)) .reader(reader)
	 * .processor((ItemProcessor<? super TempHeaders, ? extends TempHeaders>)
	 * processor()) .writer(writer) .faultTolerant()
	 * .skipPolicy(fileVerificationSkipper()) .taskExecutor(taskExecutor())
	 * .listener(stepExecutionListener) .build(); }
	 */

	@Bean
	public Step step1() {
		return stepBuilderFactory.get("step1").<Map<String, Object>, Map<String, Object>>chunk(10).reader(reader())
				.processor(processor()).writer(writer()).build();
	}

	@Bean
	public HeadersProcessor processor() {
		return new HeadersProcessor();
	}

	public void setChunkSize(String chunkSize) {
		this.chunkSize = chunkSize;
	}

	public void setMaxThread(String maxThread) {
		this.maxThread = maxThread;
	}

	public void setJobParamModel(JobParamModel buildJobParamModel) {
		this.jobParamModel = buildJobParamModel;

	}
}
