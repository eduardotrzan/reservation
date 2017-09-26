package com.island.reservation.model.entity;

import com.island.reservation.model.database.postgres.PostgreSQLEnumType;
import com.island.reservation.model.entity.enums.RoomStatus;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Set;

@Entity
@Table(name = "\"room\"")
@TypeDef(
		name = "pgsql_enum",
		typeClass = PostgreSQLEnumType.class
)
public class Room extends GenericEntity<Integer> {

	private String title;

	private RoomStatus status;

	private Set<Booking> bookings;

	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="room_id_seq")
	@SequenceGenerator(name="room_id_seq", sequenceName="room_id_seq", allocationSize=1)
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

	@Column(name = "title", nullable = false, length = 200)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false, length = 255, columnDefinition = "DEFAULT 'AVAILABLE'")
	@Type( type = "pgsql_enum" )
	public RoomStatus getStatus() {
		return status;
	}

	public void setStatus(RoomStatus status) {
		this.status = status;
	}

	@OneToMany(targetEntity = Booking.class, fetch = FetchType.LAZY)
	public Set<Booking> getBookings() {
		return bookings;
	}

	public void setBookings(Set<Booking> bookings) {
		this.bookings = bookings;
	}
}
