package org.govt.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.mongodb.core.query.Query;

import java.util.Collections;
import java.util.HashMap;

import org.govt.Authentication.JwtUtil;
import org.govt.login_message.Register;
import org.govt.model.Address;
import org.govt.model.Project;
import org.govt.model.User_Supervisor;
import org.govt.model.User_Supplier;
import org.govt.repository.UserSupervisorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class UserSupervisorService {
    private final UserSupervisorRepository userSupervisorRepository;
    private final PasswordEncoder password;

    @Autowired
    private JwtUtil jwt=new JwtUtil();
    @Autowired
    ProjectService projectService;
    @Autowired
private MongoTemplate mongoTemplate;



public List<User_Supervisor> getSupervisorsByZone(String zone) {
    return userSupervisorRepository.findByZone(zone);
}

    public UserSupervisorService(UserSupervisorRepository user){
        this.userSupervisorRepository =user;
        this.password=new BCryptPasswordEncoder();
    }

    public Register registerSupervisor(User_Supervisor user_supervisor){
        if(findByUsername(user_supervisor.getUsername())!=null){
            return new Register("User Already Exists!!!!","");
        }

        user_supervisor.setPassword(password.encode(user_supervisor.getPassword()));
        userSupervisorRepository.save(user_supervisor);
        return new Register("Registered Successfully!!!", jwt.generateToken(user_supervisor.getUsername()));
    }
    public User_Supervisor findByUsername(String username) {
        return userSupervisorRepository.findByUsername(username);
    }

    public boolean authenticateSupervisor(String username,String pass){
        User_Supervisor user1= userSupervisorRepository.findByUsername(username);
        return user1!=null && password.matches(pass,user1.getPassword());
    }

    public List<User_Supervisor> findNearestSupervisor(Address address) {
    if (address == null) {
        return getAvailableSupervisors(getAllSupervisors()); // fallback if no address provided
    }

    // Define search hierarchy (most specific → least specific)
    List<Function<Address, Map<String, String>>> searchLevels = List.of(
        // Street + ZipCode
        addr -> {
            Map<String, String> map = new HashMap<>();
            if (addr.getStreet() != null && !addr.getStreet().isBlank()) {
                map.put("street", addr.getStreet());
            }
            if (addr.getZipCode() != null && !addr.getZipCode().isBlank()) {
                map.put("zipCode", addr.getZipCode());
            }
            return map;
        },
        // City
        addr -> {
            Map<String, String> map = new HashMap<>();
            if (addr.getCity() != null && !addr.getCity().isBlank()) {
                map.put("city", addr.getCity());
            }
            return map;
        },
        // State
        addr -> {
            Map<String, String> map = new HashMap<>();
            if (addr.getState() != null && !addr.getState().isBlank()) {
                map.put("state", addr.getState());
            }
            return map;
        },
        // Country
        addr -> {
            Map<String, String> map = new HashMap<>();
            if (addr.getCountry() != null && !addr.getCountry().isBlank()) {
                map.put("country", addr.getCountry());
            }
            return map;
        }
    );

    for (Function<Address, Map<String, String>> extractor : searchLevels) {
        Map<String, String> criteria = extractor.apply(address);

        if (criteria.isEmpty()) {
            continue;
        }

        List<User_Supervisor> supervisors = findByCriteria(criteria);
        List<User_Supervisor> available = getAvailableSupervisors(supervisors);

        if (!available.isEmpty()) {
            return available;
        }
    }

    // Fallback → all available supervisors
    return getAvailableSupervisors(getAllSupervisors());
}

private List<User_Supervisor> getAvailableSupervisors(List<User_Supervisor> supervisors) {
    return supervisors.stream()
        .filter(s -> s.getConnected() == null || s.getConnected().length < 3)
        .collect(Collectors.toList());
}

    public List<User_Supervisor> findByCriteria(Map<String, String> criteria) {
    Query query = new Query();

    criteria.forEach((key, value) -> {
        if (value != null && !value.isBlank()) {
            query.addCriteria(Criteria.where("address." + key).is(value));
        }
    });

    return mongoTemplate.find(query, User_Supervisor.class);
}

private List<User_Supervisor> getAllSupervisors() {
    return userSupervisorRepository.findAll();
    }

    public List<Project> getProjectsBySupervisorId(String id) {
        return projectService.getProjectsBySupervisorId(id);
     
    }

 


}
