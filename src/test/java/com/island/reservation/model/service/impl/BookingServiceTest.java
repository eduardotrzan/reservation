package com.island.reservation.model.service.impl;

import com.island.reservation.exceptions.Error;
import com.island.reservation.exceptions.UnprocessableError;
import com.island.reservation.model.dao.IBookingDao;
import com.island.reservation.model.dao.IRoomDao;
import com.island.reservation.model.entity.Booking;
import com.island.reservation.model.service.interfaces.IGuestService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Calendar;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

public class BookingServiceTest {

	@Mock
	private IGuestService guestService;

	@Mock
	private IBookingDao bookingDao;

	@Mock
	private IRoomDao roomDao;

	@InjectMocks
	private BookingService bookingService;

	@BeforeMethod(alwaysRun = true)
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test(expectedExceptions = Error.class)
	public void isAvailable_nullStartDate_exception() throws Exception {
		assertThat(this.bookingService.isAvailable(null, null));
	}

	@Test(expectedExceptions = UnprocessableError.class)
	public void isAvailable_validStartDate_daoException() throws Exception {
		when(this.bookingDao.findAvailability(any(Calendar.class), any(Calendar.class)))
				.thenThrow(new RuntimeException());
		assertThat(this.bookingService.isAvailable(Calendar.getInstance(), null));
	}

	@Test(expectedExceptions = Error.class)
	public void isAvailable_nullStartDateNotNullEndDate_exception() throws Exception {
		assertThat(this.bookingService.isAvailable(null, Calendar.getInstance()));
	}

	@Test(expectedExceptions = UnprocessableError.class)
	public void isAvailable_validStartDateNotNullEndDate_daoException() throws Exception {
		when(this.bookingDao.findAvailability(any(Calendar.class), any(Calendar.class)))
				.thenThrow(new RuntimeException());
		assertThat(this.bookingService.isAvailable(Calendar.getInstance(), Calendar.getInstance()));
	}

	@Test
	public void isAvailable_startDateNullEndDate_hasBooking() {
		when(this.bookingDao.findAvailability(any(Calendar.class), any(Calendar.class)))
				.thenReturn(new Booking());
		try {
			assertThat(this.bookingService.isAvailable(Calendar.getInstance(), null)).isFalse();
			assertThat(this.bookingService.isAvailable(Calendar.getInstance(), Calendar.getInstance())).isFalse();
		} catch (Exception e) {
			fail("Failed on testing valid start date with null end date, having an existing booking in the period.", e);
		}
	}

	@Test
	public void isAvailable_startDateNullEndDate_hasnotBooking() {
		when(this.bookingDao.findAvailability(any(Calendar.class), any(Calendar.class)))
				.thenReturn(null);
		try {
			assertThat(this.bookingService.isAvailable(Calendar.getInstance(), null)).isTrue();
			assertThat(this.bookingService.isAvailable(Calendar.getInstance(), Calendar.getInstance())).isTrue();
		} catch (Exception e) {
			fail("Failed on testing valid start date with null end date, having an existing booking in the period.", e);
		}
	}


	@Test(expectedExceptions = Error.class)
	public void isAvailable_notNulllUuidNullStartDate_exception() throws Exception {
		assertThat(this.bookingService.isAvailable(null, null, UUID.randomUUID().toString()));
	}

	@Test(expectedExceptions = UnprocessableError.class)
	public void isAvailable_notNulllUuidValidStartDate_daoException() throws Exception {
		when(this.bookingDao.findAvailabilityExceptUuid(any(Calendar.class), any(Calendar.class), anyString()))
				.thenThrow(new RuntimeException());
		assertThat(this.bookingService.isAvailable(Calendar.getInstance(), null, UUID.randomUUID().toString()));
	}

	@Test(expectedExceptions = Error.class)
	public void isAvailable_notNulllUuidNullStartDateNotNullEndDate_exception() throws Exception {
		assertThat(this.bookingService.isAvailable(null, Calendar.getInstance(), UUID.randomUUID().toString()));
	}

	@Test(expectedExceptions = UnprocessableError.class)
	public void isAvailable_notNulllUuidValidStartDateNotNullEndDate_daoException() throws Exception {
		when(this.bookingDao.findAvailabilityExceptUuid(any(Calendar.class), any(Calendar.class), anyString()))
				.thenThrow(new RuntimeException());
		assertThat(this.bookingService.isAvailable(Calendar.getInstance(), Calendar.getInstance(), UUID.randomUUID().toString()));
	}

	@Test
	public void isAvailable_notNulllUuiStartDateNullEndDate_hasBooking() {
		when(this.bookingDao.findAvailabilityExceptUuid(any(Calendar.class), any(Calendar.class), anyString()))
				.thenReturn(new Booking());
		try {
			assertThat(this.bookingService.isAvailable(Calendar.getInstance(), null, UUID.randomUUID().toString()))
					.isFalse();
			assertThat(this.bookingService.isAvailable(Calendar.getInstance(), Calendar.getInstance(), UUID.randomUUID().toString()))
					.isFalse();
		} catch (Exception e) {
			fail("Failed on testing not null UUID valid start date with null end date, having an existing booking in the period.", e);
		}
	}

	@Test
	public void isAvailable_notNulllUuiStartDateNullEndDate_hasnotBooking() {
		when(this.bookingDao.findAvailabilityExceptUuid(any(Calendar.class), any(Calendar.class), anyString()))
				.thenReturn(null);
		try {
			assertThat(this.bookingService.isAvailable(Calendar.getInstance(), null, UUID.randomUUID().toString()))
					.isTrue();
			assertThat(this.bookingService.isAvailable(Calendar.getInstance(), Calendar.getInstance(), UUID.randomUUID().toString()))
					.isTrue();
		} catch (Exception e) {
			fail("Failed on testing not null UUID valid start date with null end date, having an existing booking in the period.", e);
		}
	}

}