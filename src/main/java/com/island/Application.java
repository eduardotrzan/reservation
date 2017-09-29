package com.island;

import com.island.reservation.system.BookingConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = {"com.island.reservation"})
@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.island.reservation.model.dao")
@EntityScan(basePackages = "com.island.reservation.model.entity")
public class Application implements CommandLineRunner {

	@PostConstruct
	void started() {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	}

	@Autowired
	private BookingConfig config;

	private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {
		SpringApplication.run(Application.class);
	}

	@Override
	public void run(String... strings) {
		LOGGER.info("Running " + this.config.getServiceName() + " system....");
	}
}
