package com.sam.boot.batch.config;

import com.sam.boot.batch.faker.FakeDataGenerator;
import com.sam.boot.batch.model.Employee;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class BatchConfig {

    @Autowired
    private JobBuilderFactory jbf;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    FakeDataGenerator fakeDataGenerator;

    @Bean
    public Job job(){
        return  jbf.get("job1")
                .incrementer(new RunIdIncrementer())
                .start(step())
                .build();
    }

    @Bean
    public Step step(){
        return stepBuilderFactory.get("step1")
                .<Employee, Employee>chunk(100)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build()
                ;

    }

    @Bean
    public ItemReader<Employee> reader(){

        fakeDataGenerator.generate();
        FlatFileItemReader<Employee> reader = new FlatFileItemReader<>();
        reader.setResource(new ClassPathResource("employee.csv"));

        reader.setLinesToSkip(1);  // skip first row

        DefaultLineMapper<Employee> mapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setNames("id","firstname","lastname","company","address","salary");

        BeanWrapperFieldSetMapper<Employee> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(Employee.class);

        mapper.setLineTokenizer(lineTokenizer);
        mapper.setFieldSetMapper(fieldSetMapper);

        reader.setLineMapper(mapper);
        return reader;
    }

    @Bean
    public ItemProcessor<Employee, Employee> processor(){
        return (p)->{
                p.setSalary(p.getSalary()+p.getSalary()*10/100);
                return p;
        };
    }

    @Bean
    public ItemWriter<Employee> writer(){

        JdbcBatchItemWriter<Employee> writer = new JdbcBatchItemWriter<>();

        writer.setDataSource(dataSource());
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Employee>());
        writer.setSql("INSERT INTO EMPLOYEE (id,firstname,lastname,company,address,salary) VALUES (:id,:firstname,:lastname,:company,:address,:salary)");

        return writer;
    }

    @Bean
    public DataSource dataSource(){

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl("jdbc:h2:tcp://localhost/~/test");
        dataSource.setUsername("sa");
        //dataSource.setPassword();

        return dataSource;
    }
}
