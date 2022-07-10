package com.sam.boot.batch;

import com.sam.boot.batch.config.BatchConfig;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
class SpringBatchCsvToDbAppTests {

	@Autowired
	private JobLauncher launcher;

	@Autowired
	private Job job;

	@Test
	public  void testBatch() throws JobInstanceAlreadyCompleteException,
			JobExecutionAlreadyRunningException,
			JobParametersInvalidException,
			JobRestartException {

		launcher.run(job, new JobParametersBuilder()
				.addLong("time",System.currentTimeMillis()).toJobParameters());
	}

}
