package com.island.reservation.model.entity;

import java.io.Serializable;
import java.util.Calendar;

public abstract class GenericEntity<ID> implements Serializable {

	private ID id;
	private Calendar createDate = Calendar.getInstance();
	private Calendar updateDate;
	private Calendar deleteDate;
	private boolean isDeleted;

	public ID getId() {
		return this.id;
	}

	public void setId(final ID id) {
		this.id = id;
	}

	public Calendar getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Calendar createDate) {
		this.createDate = createDate;
	}

	public Calendar getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Calendar updateDate) {
		this.updateDate = updateDate;
	}

	public Calendar getDeleteDate() {
		return deleteDate;
	}

	public void setDeleteDate(Calendar deleteDate) {
		this.deleteDate = deleteDate;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean deleted) {
		isDeleted = deleted;
	}

	@Override
	public final boolean equals(final Object object) {
		if (object == null) {
			return Boolean.FALSE;
		}

		if (this == object) {
			return Boolean.TRUE;
		}

		if ((object instanceof GenericEntity)) {
			try {
				GenericEntity<ID> otherObject = (GenericEntity<ID>) object;
				ID thisId = this.getId();
				ID oId = otherObject.getId();

				if (thisId == null) {
					return Boolean.FALSE;
				}

				if (oId == null) {
					return Boolean.FALSE;
				}

				if (!thisId.equals(oId)) {
					return Boolean.FALSE;
				}
			} catch (Exception e) {
				return Boolean.FALSE;
			}

			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}
	}

	@Override
	public final int hashCode() {
		final int prime = 31;
		int result = 1;
		ID thisId = this.getId();

		if ((thisId == null)) {
			result = (prime * result);
		} else {
			result = (prime * result) + thisId.hashCode();
		}
		return result;
	}
}
