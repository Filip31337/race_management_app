package org.brajnovic.repository;

import org.brajnovic.entity.ApplicationById;
import org.springframework.data.cassandra.repository.CassandraRepository;

import java.util.UUID;

public interface ApplicationByIdRepository extends CassandraRepository<ApplicationById, UUID> {
}
