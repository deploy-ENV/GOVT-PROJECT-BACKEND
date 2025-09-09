package org.govt.repository;

import org.govt.model.User_contractor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserContractorRepository extends MongoRepository<User_contractor,String>{
    User_contractor findByUsername(String username);
}
