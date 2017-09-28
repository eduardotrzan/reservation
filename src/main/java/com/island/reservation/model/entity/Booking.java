package com.island.reservation.model.entity;

import com.island.reservation.model.database.postgres.PostgreSQLEnumType;
import com.island.reservation.model.entity.enums.BookingStatus;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.Calendar;
import java.util.UUID;

@Entity
@Table(name = "\"booking\"")
@TypeDef(
		name = "pgsql_enum",
		typeClass = PostgreSQLEnumType.class
)
@NamedQuery(
		name = "Booking.findAvailability",
		query = "SELECT b FROM Booking b " +
					"WHERE (?1 BETWEEN b.startDate AND b.endDate) " +
					"OR (?2 BETWEEN b.startDate AND b.endDate) "
)
public class Booking extends GenericEntity<Integer> {

	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="booking_id_seq")
	@SequenceGenerator(name="booking_id_seq", sequenceName="booking_id_seq", allocationSize=1)
	@Column(name = "id")
	public Integer getId() {
		return super.getId();
	}

	private String uuid = UUID.randomUUID().toString();

	private Calendar startDate;

	private Calendar endDate;

	private BookingStatus status;

	private Guest guest;

	private Room room;

	@Column(name = "uuid", nullable = false, length = 200)
	public String getUuid() {
		return uuid;
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
	@Type( type = "pgsql_enum" )
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
