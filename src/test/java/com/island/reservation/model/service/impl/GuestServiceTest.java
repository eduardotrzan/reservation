package com.island.reservation.model.service.impl;

import com.island.reservation.exceptions.Error;
import com.island.reservation.exceptions.ErrorCode;
import com.island.reservation.model.dao.IGuestDao;
import com.island.reservation.model.entity.Guest;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

public class GuestServiceTest {

	@Mock
	private IGuestDao guestDao;

	@InjectMocks
	private GuestService guestService;

	@BeforeMethod(alwaysRun = true)
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test(expectedExceptions = Error.class)
	public void saveIfNotExists_nullGuest_exception() throws Exception {
		assertThat(this.guestService.saveIfNotExists(null));
	}

	@Test
	public void saveIfNotExists_GuestValidateError_exception() {
		Error error = new Error(
				ErrorCode.INVALID_PARAMETER
				, "Guest Information Error"
				, "Guest contains one or more invalid information."
				, ""
		);

		Guest guest = new Guest();
		this.guestSaveAssertions(guest, error);

		guest.setFirstName("Test");
		this.guestSaveAssertions(guest, error);

		guest.setLastName("Test");
		this.guestSaveAssertions(guest, error);

		guest.setLastName(null);
		guest.setEmail("Test");
		this.guestSaveAssertions(guest, error);

		guest.setEmail("Test@Test");
		this.guestSaveAssertions(guest, error);

		guest.setEmail("@Test");
		this.guestSaveAssertions(guest, error);

		guest.setEmail("@Test.com");
		this.guestSaveAssertions(guest, error);

		guest.setLastName("Test");
		guest.setEmail("Test");
		this.guestSaveAssertions(guest, error);

		guest.setEmail("Test@Test");
		this.guestSaveAssertions(guest, error);

		guest.setEmail("@Test");
		this.guestSaveAssertions(guest, error);

		guest.setEmail("@Test.com");
		this.guestSaveAssertions(guest, error);


		guest = new Guest();
		guest.setLastName("Test");
		this.guestSaveAssertions(guest, error);

		guest.setEmail("Test");
		this.guestSaveAssertions(guest, error);

		guest.setEmail("Test@Test");
		this.guestSaveAssertions(guest, error);

		guest.setEmail("@Test");
		this.guestSaveAssertions(guest, error);

		guest.setEmail("@Test.com");
		this.guestSaveAssertions(guest, error);


		guest = new Guest();
		guest.setEmail("Test");
		this.guestSaveAssertions(guest, error);

		guest.setEmail("Test@Test");
		this.guestSaveAssertions(guest, error);

		guest.setEmail("@Test");
		this.guestSaveAssertions(guest, error);

		guest.setEmail("@Test.com");
		this.guestSaveAssertions(guest, error);

		guest = new Guest();
		guest.setFirstName("Test");
		guest.setLastName("Test");
		guest.setEmail("test@test.com");

		RuntimeException exception = new RuntimeException("General exception");
		Error dbError = new Error(
				ErrorCode.DATABASE
				, "Guest Information Error"
				, "An error has happened while saving if not exist Guest in the DB."
				, exception.getMessage()
		);
		when(this.guestDao.findByEmail(anyString()))
				.thenReturn(null);
		when(this.guestDao.save(any(Guest.class))).thenThrow(exception);
		this.guestSaveAssertions(guest, dbError);

		when(this.guestDao.findByEmail(anyString())).thenThrow(exception);
		this.guestSaveAssertions(guest, dbError);

	}

	@Test
	public void saveIfNotExists_GuestValidate_noErrors() {
		Guest guest = new Guest();
		guest.setFirstName("Test");
		guest.setLastName("Test");
		guest.setEmail("test@test.com");

		try {
			when(this.guestDao.findByEmail(anyString()))
					.thenReturn(guest);
			Guest existingGuest = this.guestService.saveIfNotExists(guest);
			this.guestSaveSuccessAssertions(guest, existingGuest);
		} catch (Exception e) {
			fail("Save Guest should not produce an exception.", e);
		}

		try {
			when(this.guestDao.findByEmail(anyString()))
					.thenReturn(null);
			when(this.guestDao.save(any(Guest.class)))
					.thenReturn(guest);
			Guest existingGuest = this.guestService.saveIfNotExists(guest);
			this.guestSaveSuccessAssertions(guest, existingGuest);
		} catch (Exception e) {
			fail("Save Guest should not produce an exception.", e);
		}
	}

	private void guestSaveAssertions(Guest guest, Error error) {
		try {
			assertThat(this.guestService.saveIfNotExists(guest));
			fail("Save with all guest attributes null should produce an exception");
		} catch (Error e) {
			assertThat(e.getErrorCode()).isEqualTo(error.getErrorCode());
			assertThat(e.getTitle()).isEqualTo(error.getTitle());
			assertThat(e.getDescription()).isEqualTo(error.getDescription());
			assertThat(e.getDetails()).isNotBlank();
		} catch (Exception e) {
			fail("Save with all guest attributes null should produce an Error exception type.", e);
		}
	}

	private void guestSaveSuccessAssertions(Guest expectedGuest, Guest actualGuest) {
		assertThat(actualGuest.getFirstName()).isEqualTo(expectedGuest.getFirstName());
		assertThat(actualGuest.getLastName()).isEqualTo(expectedGuest.getLastName());
		assertThat(actualGuest.getEmail()).isEqualTo(expectedGuest.getEmail());
	}

}