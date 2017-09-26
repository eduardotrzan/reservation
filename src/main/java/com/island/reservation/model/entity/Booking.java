package com.island.reservation.model.entity;

import com.island.reservation.model.entity.enums.BookingStatus;
import org.springframework.context.annotation.Profile;

import javax.persistence.*;
import java.util.Calendar;

@Entity
@Table(name = "\"booking\"")
public class Booking extends GenericEntity<Integer> {

	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="booking_id_seq")
	@SequenceGenerator(name="booking_id_seq", sequenceName="booking_id_seq", allocationSize=1)
	@Column(name = "id")
	public Integer getId() {
		return super.getId();
	}

	private String uuid;

	private Calendar startDate;

	private Calendar endDate;

	private BookingStatus status;

	private Guest guest;

	private Room room;

	@Column(name = "uuid", nullable = false, length = 200)
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "start_date")
	public Calendar getStartDate() {
		return startDate;
	}

	public void setStartDate(Calendar startDate) {
		this.startDate = startDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "end_date")
	public Calendar getEndDate() {
		return endDate;
	}

	public void setEndDate(Calendar endDate) {
		this.endDate = endDate;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false, length = 255, columnDefinition = "DEFAULT 'PENDING'")
	public BookingStatus getStatus() {
		return status;
	}

	public void setStatus(BookingStatus status) {
		this.status = status;
	}

	@ManyToOne(targetEntity = Guest.class, fetch = FetchType.LAZY)
	@JoinColumn(name="guest_id")
	public Guest getGuest() {
		return guest;
	}

	public void setGuest(Guest guest) {
		this.guest = guest;
	}

	@ManyToOne(targetEntity = Room.class, fetch = FetchType.LAZY)
	@JoinColumn(name="room_id")
	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}
}
