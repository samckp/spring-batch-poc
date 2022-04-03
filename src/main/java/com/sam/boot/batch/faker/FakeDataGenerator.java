package com.sam.boot.batch.faker;

import au.com.anthonybruno.Gen;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class FakeDataGenerator {

    @Value("${numberOfRecords}")
    private Integer numOfRecords;

    public void generate() {

        Faker faker = new Faker();
        Gen.start()
                .addField("Id", () -> faker.number().numberBetween(1,99999))
                .addField("Name", () -> faker.name().firstName())
                .addField("Name", () -> faker.name().lastName())
                .addField("Company", () -> faker.company().name())
                .addField("Address", () -> faker.address().fullAddress())
                .addField("Salary", () -> faker.number().randomNumber(5,true))

                .generate(numOfRecords)
                .asCsv()
                .toFile("src/main/resources/employee.csv");
    }
}
