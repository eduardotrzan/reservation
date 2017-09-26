package com.island.reservation.model.entity;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Set;

@Entity
@Table(name = "\"guest\"")
public class Guest extends GenericEntity<Integer> {

	private String firstName;

	private String lastName;

	private String email;

	private Set<Booking> bookings;

	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="guest_id_seq")
	@SequenceGenerator(name="guest_id_seq", sequenceName="guest_id_seq", allocationSize=1)
	@Column(name = "id")
	public Integer getId() {
		return super.getId();
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_date", nullable = true, columnDefinition = "DEFAULT CURRENT_TIMESTAMP")
	public Calendar getCreateDate() {
		return super.getCreateDate();
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "update_date")
	public Calendar getUpdateDate() {
		return super.getUpdateDate();
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "delete_date")
	public Calendar getDeleteDate() {
		return super.getDeleteDate();
	}

	@Column(name = "is_deleted")
	public boolean isDeleted() {
		return super.isDeleted();
	}

	@Column(name = "first_name", nullable = false, length = 200)
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Column(name = "last_name", nullable = false, length = 200)
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Column(name = "email", nullable = false, length = 200)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@OneToMany(targetEntity = Booking.class, fetch = FetchType.LAZY)
	public Set<Booking> getBookings() {
		return bookings;
	}

	public void setBookings(Set<Booking> bookings) {
		this.bookings = bookings;
	}
}