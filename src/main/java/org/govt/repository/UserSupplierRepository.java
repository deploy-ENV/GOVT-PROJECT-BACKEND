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
 List<User_Supplier> findByAddress_StreetAndAddress_ZipCode(String street, String zipCode);
    List<User_Supplier> findByAddress_City(String city);
    List<User_Supplier> findByAddress_State(String state);
    List<User_Supplier> findByAddress_Country(String country);
    List<User_Supplier> findByAddress_ZipCode(String zipCode);
}
