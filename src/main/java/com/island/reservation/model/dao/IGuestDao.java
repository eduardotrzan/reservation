package com.island.reservation.model.dao;

import com.island.reservation.model.entity.Guest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.stereotype.Repository;

@Repository
@RepositoryDefinition(domainClass = Guest.class, idClass = Integer.class)
public interface IGuestDao extends CrudRepository<Guest, Integer> {
}