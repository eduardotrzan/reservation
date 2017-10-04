package com.island.reservation.model.service.impl;

import com.island.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class BookingServiceIT {

	@Autowired
	private BookingService bookingService;



	@Test
	public void isAvailable_nullStartDate_exception() throws Exception {
		assertThat(this.bookingService).isNotNull();
		assertThat(this.bookingService.isAvailable(null, null));
	}

}