package org.brajnovic.repository;

import org.brajnovic.entity.ApplicationById;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ApplicationByIdRepository extends CassandraRepository<ApplicationById, UUID> {

    @Query("SELECT * FROM application_by_id WHERE useremail=?0 ALLOW FILTERING")
    List<ApplicationById> findByUserEmail(String userEmail);
}
