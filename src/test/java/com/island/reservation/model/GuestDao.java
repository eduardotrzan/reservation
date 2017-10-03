package com.island.reservation.model;

import com.island.reservation.model.dao.IGuestDao;
import com.island.reservation.model.entity.Guest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
public class GuestDao {

	@Autowired
	private IGuestDao guestDao;

	@Test
	public void shouldSaveAndFetchPerson() throws Exception {
		Guest guest = new Guest();
		this.guestDao.save(guest);
	}
}
