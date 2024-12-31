package com.tax.app;

import java.util.Properties;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;


@SpringBootApplication
@EnableMongoRepositories
public class IncomeTaxCalculatorApplication {

	public static void main(String[] args) {
		Properties props = new Properties();
		String mongoPass = "7WYhwFezKce1W6M0";
		String mongoDBUrl = "mongodb+srv://nayan97:" + mongoPass
				+ "@cluster0.cgcpm.mongodb.net/income_tax_cal_db?retryWrites=true&w=majority";
		props.put("server.port", "8081");
		props.put("spring.data.mongodb.uri", mongoDBUrl);
		props.put("spring.data.mongodb.databasee", "income_tax_cal_db");
		props.put("spring.jpa.defer-datasource-initialization", "true");
		new SpringApplicationBuilder(IncomeTaxCalculatorApplication.class).properties(props).run(args);

	}

}
