package com.island.reservation.controller.wrappers;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

abstract class GenericWs<T, K> {

	List<T> toBean(@NotNull List<K> ks) {
		List<T> bookings = new ArrayList<>();
		for (K k : ks) {
			bookings.add(this.toBean(k));
		}
		return bookings;
	}

	List<K> toWs(List<T> ts) {
		List<K> ks = new ArrayList<>();
		for (T t : ts) {
			ks.add(toWs(t));
		}
		return ks;
	}

	abstract T toBean(@NotNull K k);
	abstract K toWs(T t);
}