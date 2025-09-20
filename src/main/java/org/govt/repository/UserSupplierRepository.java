package org.govt.repository;

import java.util.List;

import org.govt.model.Address;
import org.govt.model.User_Supplier;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSupplierRepository extends MongoRepository<User_Supplier,String> {
    User_Supplier findByUsername(String username);
    List<User_Supplier> findByAddress(Address pincode);
List<User_Supplier> findByApprovedTrue();
}
