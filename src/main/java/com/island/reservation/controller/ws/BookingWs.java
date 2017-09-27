package com.island.reservation.controller.ws;

import com.fasterxml.jackson.annotation.*;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonRootName(value = "booking")
@JsonPropertyOrder({"uuid", "status", "startDate", "endDate", "createDate", "updateDate"})
public class BookingWs {

	@JsonProperty("bookingIdentifier")
	private String uuid;

	private String status;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm a z")
	private Date createDate;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm a z")
	private Date updateDate;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm a z")
	private Date startDate;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm a z")
	private Date endDate;

	private GuestWs guest;

	private RoomWs room;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public GuestWs getGuest() {
		return guest;
	}

	public void setGuest(GuestWs guest) {
		this.guest = guest;
	}

	public RoomWs getRoom() {
		return room;
	}

	public void setRoom(RoomWs room) {
		this.room = room;
	}
}