package com.island;

import com.island.reservation.model.dao.IBookingDao;
import com.island.reservation.model.dao.IGuestDao;
import com.island.reservation.model.dao.IRoomDao;
import com.island.reservation.model.entity.Booking;
import com.island.reservation.model.entity.Guest;
import com.island.reservation.model.entity.Room;
import com.island.reservation.model.entity.enums.BookingStatus;
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
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.UUID;

@Configuration
@EnableAutoConfiguration
@ComponentScan
//		(basePackages = {"com.island"})
@SpringBootApplication
@EnableJpaRepositories
//		(basePackages = "com.island.reservation.model.dao")
@EntityScan
//		(basePackages = "com.island.reservation.model.entity")
public class Application implements CommandLineRunner {

	@Autowired
	private IRoomDao roomDao;

	@Autowired
	private IGuestDao guestDao;

	@Autowired
	private IBookingDao bookingDao;

	@PostConstruct
	void started() {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	}

	@Autowired
	private BookingConfig config;

	private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	@Transactional
	public void run(String... strings) {
		LOGGER.info("Running " + this.config.getServiceName() + " system....");
		Room room = roomDao.findByTitle("Campsite");

		Guest guest = new Guest();
		guest.setFirstName("Eduardo");
		guest.setLastName("Trzan");
		guest.setEmail("eduardo@eduardo.com");
		guest = guestDao.save(guest);

		Booking booking = new Booking();
		booking.setUuid(UUID.randomUUID().toString());
		booking.setGuest(guest);
		booking.setRoom(room);
		booking.setStatus(BookingStatus.CONFIRMED);

		Calendar startDate = Calendar.getInstance();
		startDate.add(Calendar.MONTH, 1);
		booking.setStartDate(startDate);
		Calendar endDate = Calendar.getInstance();
		endDate.add(Calendar.MONTH, 1);
		endDate.add(Calendar.DAY_OF_MONTH, 3);
		booking.setEndDate(endDate);
		booking = bookingDao.save(booking);

		LOGGER.info(booking.toString());
	}
}
