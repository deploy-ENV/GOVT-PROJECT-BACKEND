package org.govt.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// import javax.management.Query; // Removed incorrect import
import org.springframework.data.mongodb.core.query.Query;

import org.govt.Authentication.JwtUtil;
import org.govt.login_message.Register;
import org.govt.model.Products;
import org.govt.model.Project;
import org.govt.model.User_Supplier;
import org.govt.repository.ProjectRepository;
import org.govt.repository.UserSupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.security.access.method.P;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserSupplierService {
    @Autowired
    private UserSupplierRepository userSupplierRepository;
    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private  PasswordEncoder password;
    @Autowired
    private ProjectRepository projectRepo;
    @Autowired
    private UserSupplierRepository supplierRepo;

    @Autowired
    JwtUtil jwt =new JwtUtil();
public List<User_Supplier> autoFetchSuppliers(String projectId) {
    Project project = projectRepo.findById(projectId).orElseThrow();

    // Simple example: match by pincode (zone)
    String zone = project.getLocation().getZipCode();
    return supplierRepo.findByAddress(project.getLocation());
    
}
    public Register registerSupplier(User_Supplier userSupplier){
        if(findByUsername(userSupplier.getUsername())!=null){
            return new Register("User Already Exists!!","");
        }
        userSupplier.setPassword(password.encode(userSupplier.getPassword()));
        userSupplierRepository.save(userSupplier);
        return new Register("User Registered!!!", jwt.generateToken(userSupplier.getUsername()));
    }
    public User_Supplier findByUsername(String username) {
        return userSupplierRepository.findByUsername(username);
    }

    public boolean authenticateSupplier(String username,String password1){
        BCryptPasswordEncoder pass=new BCryptPasswordEncoder();
        User_Supplier supplier= userSupplierRepository.findByUsername(username);
        return supplier!=null && password.matches(password1,supplier.getPassword());
    }
     public List<User_Supplier> findByStreetAndZipCode(String street, String zipCode) {
        return userSupplierRepository.findByAddress_StreetAndAddress_ZipCode(street, zipCode);
    }

    public List<User_Supplier> findByCity(String city) {
        return userSupplierRepository.findByAddress_City(city);
    }

    public List<User_Supplier> findByState(String state) {
        return userSupplierRepository.findByAddress_State(state);
    }

    public List<User_Supplier> findByCountry(String country) {
        return userSupplierRepository.findByAddress_Country(country);
    }

    public List<User_Supplier> getAllSuppliers() {
        return userSupplierRepository.findAll();
    }
      public List<User_Supplier> findByCriteria(Map<String, String> criteria) {
        Query query = new Query();
        criteria.forEach((field, value) -> {
            query.addCriteria(Criteria.where("address." + field).is(value));
        });
        return mongoTemplate.find(query, User_Supplier.class);
    }

      // Add product to supplier catalog
    public User_Supplier addProduct(String supplierId, Products product) {
        User_Supplier supplier = userSupplierRepository.findById(supplierId)
                .orElseThrow(() -> new RuntimeException("Supplier not found"));

        if (supplier.getCatalogProducts() == null) {
            supplier.setCatalogProducts(new ArrayList<>());
        }

        supplier.getCatalogProducts().add(product);
        return userSupplierRepository.save(supplier);
    }

    // Delete product from catalog
    public User_Supplier deleteProduct(String supplierId, String productId) {
        User_Supplier supplier = userSupplierRepository.findById(supplierId)
                .orElseThrow(() -> new RuntimeException("Supplier not found"));

        supplier.getCatalogProducts().removeIf(p -> p.getId().equals(productId));
        return userSupplierRepository.save(supplier);
    }
    // Change product availability
    public User_Supplier updateProductAvailability(String supplierId, String productId, boolean available) {
        User_Supplier supplier = userSupplierRepository.findById(supplierId)
                .orElseThrow(() -> new RuntimeException("Supplier not found"));

        supplier.getCatalogProducts().forEach(p -> {
            if (p.getId().equals(productId)) {
                p.setAvailable(available);
            }
        });

        return userSupplierRepository.save(supplier);
    }

    // Update product details (optional)
    public User_Supplier updateProduct(String supplierId, Products updatedProduct) {
        User_Supplier supplier = userSupplierRepository.findById(supplierId)
                .orElseThrow(() -> new RuntimeException("Supplier not found"));

        supplier.getCatalogProducts().replaceAll(p ->
                p.getId().equals(updatedProduct.getId()) ? updatedProduct : p
        );

        return userSupplierRepository.save(supplier);
    }




}
