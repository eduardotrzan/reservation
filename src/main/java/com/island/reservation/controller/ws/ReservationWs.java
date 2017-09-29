package com.island.reservation.controller.ws;

import com.fasterxml.jackson.annotation.*;
import com.island.reservation.ConversionUtils;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonRootName(value = "reservation")
@JsonPropertyOrder({"uuid", "status", "startDate", "endDate", "createDate", "updateDate"})
public class ReservationWs {

	@JsonProperty("bookingIdentifier")
	private String uuid;

	private String firstName;

	private String lastName;

	@Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\."
			+"[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@"
			+"(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?",
			message="Email is has not a valid format. Expected as: abc@abc.com")
	private String email;

	@NotNull(message = "Start Date cannot be null")
	@Future(message = "Start Date cannot be in the Past.")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "UTC")
	private Date startDate;

	@NotNull(message = "End Date cannot be null")
	@Future(message = "End Date cannot be in the Past.")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "UTC")
	private Date endDate;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = ConversionUtils.noon(startDate).getTime();
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = ConversionUtils.noon(endDate).getTime();;
	}
}