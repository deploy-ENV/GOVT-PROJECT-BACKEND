package org.govt.repository;
import org.govt.model.User_govt;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserGovtRepository extends MongoRepository<User_govt,String> {
    User_govt findByUsername(String username);
}
