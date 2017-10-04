package com.island.reservation;

import com.island.Application;
import com.island.reservation.model.service.impl.BookingService;
import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.images.builder.ImageFromDockerfile;

import java.sql.Connection;
import java.sql.DriverManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@ActiveProfiles("it")
public class AbstractIntegrationTests {

	private static final String DB_NAME = "reservation.public";
	private static final String USER = "sa";
	private static final String PWD = "123";

//	@Rule
//	public PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:latest")
//			.withDatabaseName(DB_NAME)
//			.withUsername(USER)
//			.withPassword(PWD)
//			;

	@Rule
	public GenericContainer postgres = new GenericContainer(
			new ImageFromDockerfile()
					.withFileFromClasspath("Dockerfile", "Dockerfile"))
			;

	@Autowired
	private BookingService bookingService;

	@Before
	public void setUpDatabase() {
		try {
			String jdbcUrl = this.postgres.getContainerName();
			Connection connection = DriverManager.getConnection(jdbcUrl);

			Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));

			Liquibase liquibase = new liquibase.Liquibase("liquibase/db.changelog-master.xml", new ClassLoaderResourceAccessor(), database);

			liquibase.update(new Contexts(), new LabelExpression());
		} catch (Exception e) {
			fail("Failed on Setting up Database.", e);
		}
	}

	@Test
	public void isAvailable_nullStartDate_exception() throws Exception {
		assertThat(postgres).isNotNull();
		assertThat(this.bookingService).isNotNull();
		assertThat(this.bookingService.isAvailable(null, null));
	}
}
