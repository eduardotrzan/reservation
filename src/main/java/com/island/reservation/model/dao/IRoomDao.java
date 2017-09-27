package com.island.reservation.model.dao;

import com.island.reservation.model.entity.Room;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.stereotype.Repository;

@Repository
@RepositoryDefinition(domainClass = Room.class, idClass = Integer.class)
public interface IRoomDao extends CrudRepository<Room, Integer> {
	public Room findByTitle(String title);
}