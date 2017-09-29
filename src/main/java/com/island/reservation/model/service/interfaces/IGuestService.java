package com.island.reservation.model.service.interfaces;

import com.island.reservation.exceptions.Error;
import com.island.reservation.model.entity.Guest;

public interface IGuestService extends IService {

	Guest saveIfNotExists(Guest guest) throws Error;


}
