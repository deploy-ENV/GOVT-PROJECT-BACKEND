package org.govt.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.govt.model.User_Supervisor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSupervisorRepository extends MongoRepository<User_Supervisor, String> {
    User_Supervisor findByUsername(String username);

    List<User_Supervisor> findByZone(String zone);

    List<User_Supervisor> findByAddress_ZipCode(String zipCode);

    Optional<User_Supervisor> findById(String id);

}
