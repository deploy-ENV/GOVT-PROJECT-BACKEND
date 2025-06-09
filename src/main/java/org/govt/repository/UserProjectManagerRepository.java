package org.govt.repository;

import org.govt.model.User_ProjectManager;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProjectManagerRepository extends MongoRepository<User_ProjectManager,String> {
    User_ProjectManager findByUsername(String username);
}
