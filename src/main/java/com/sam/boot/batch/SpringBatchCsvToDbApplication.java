package com.sam.boot.batch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBatchProcessing
public class SpringBatchCsvToDbApplication {

	public static void main(String[] args)
	{
		SpringApplication.run(SpringBatchCsvToDbApplication.class, args);
	}

}
