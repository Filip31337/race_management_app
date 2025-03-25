package org.brajnovic.repository;

import org.brajnovic.entity.RaceById;
import org.springframework.data.cassandra.repository.CassandraRepository;

import java.util.UUID;

public interface RaceByIdRepository extends CassandraRepository<RaceById, UUID> {
}
