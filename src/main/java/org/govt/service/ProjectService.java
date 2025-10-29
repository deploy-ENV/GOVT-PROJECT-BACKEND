package org.govt.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.govt.Enums.ProjectStatus;
import org.govt.model.Address;
import org.govt.model.Bid;
import org.govt.model.Project;
import org.govt.model.ProjectProgress;
import org.govt.model.User_Supplier;
import org.govt.repository.BidRepository;
import org.govt.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mongodb.Function;

@Service
public class ProjectService {
  @Autowired
private ProjectRepository projectRepository;
@Autowired
private BidRepository bidRepository;
@Autowired
private UserSupplierService supplierService; 
@Autowired
private BidService bidService;

// Create a new project
public Project createProject(Project project, String pmId, String departmentId, String pmName) {
    project.setProjectManagerId(pmId);
    project.setDepartmentId(departmentId);
    project.setCreatedByName(pmName);
    project.setCreatedAt(LocalDate.now().toString());
    project.setStatus(ProjectStatus.BIDDING); // default to BIDDING when created

    return projectRepository.save(project);
}

// Get all projects posted by this PM
public List<Project> listMyProjects(String pmId) {
    return projectRepository.findByProjectManagerId(pmId);
}

// (Optional) Get a single project by ID
public Project getById(String id) {
    return projectRepository.findById(id).orElseThrow(() -> new RuntimeException("Project not found"));
}
public Project finalizeProjectAssignments(String projectId, String contractorId, String supervisorId, List<String> supplierId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        // Contractor selection (via BidService)
        bidService.acceptContractorBid(projectId, contractorId);

       

        // Update project
        project.setAssignedContractorId(contractorId);
        project.setAssignedSupervisorId(supervisorId);
        List<String> supplierIds = supplierId;
        project.setAssignedSupplierIds(supplierIds);
        project.setStatus(ProjectStatus.IN_PROGRESS); // Execution mode
        return projectRepository.save(project);
    }

// Update project progress status sequentially
public Project updateProjectProgress(String projectId, int stepIndex, String status) {
    Project project = projectRepository.findById(projectId)
                          .orElseThrow(() -> new RuntimeException("Project not found"));

    List<ProjectProgress> steps = project.getProgressSteps();

    if (stepIndex < 0 || stepIndex >= steps.size()) {
        throw new RuntimeException("Invalid progress step index");
    }

    ProjectProgress current = steps.get(stepIndex);

    if ("LOCKED".equals(current.getStatus())) {
        throw new RuntimeException("This progress step is locked until the previous one is completed.");
    }

    current.setStatus(status);

    // Unlock the next step if this one is completed
    if ("COMPLETED".equals(status) && stepIndex + 1 < steps.size()) {
        ProjectProgress next = steps.get(stepIndex + 1);
        if ("LOCKED".equals(next.getStatus())) {
            next.setStatus("NOT_STARTED");
        }
    }

    return projectRepository.save(project);
}

public List<Project> listAllProjects() {
    return projectRepository.findAll();
}

public void deleteProject(String id) {
    projectRepository.deleteById(id);
}

public void deleteAllProjects() {
    projectRepository.deleteAll();
}

public Project updateProject(Project updatedProject) {
    return projectRepository.save(updatedProject);
}

public List<Project> getProjectsBySupervisorId(String id) {
    return projectRepository.findByAssignedSupervisorId(id);
}


public List<User_Supplier> findNearestSupplier(Address address) {
    // Define search hierarchy (most specific → least specific)
    List<Function<Address, Map<String, String>>> searchLevels = List.of(
        // Street + ZipCode
        addr -> Map.of("street", addr.getStreet(), "zipCode", addr.getZipCode()),
        // City
        addr -> Map.of("city", addr.getCity()),
        // State
        addr -> Map.of("state", addr.getState()),
        // Country
        addr -> Map.of("country", addr.getCountry())
    );

    for (Function<Address, Map<String, String>> extractor : searchLevels) {
        Map<String, String> criteria = extractor.apply(address);

        // Skip if any required field is null/blank
        if (criteria.values().stream().anyMatch(v -> v == null || v.isBlank())) {
            continue;
        }

        // Query suppliers dynamically
        List<User_Supplier> suppliers = supplierService.findByCriteria(criteria);
        if (!suppliers.isEmpty()) {
            return suppliers;
        }
    }

    // Fallback → all suppliers
    return supplierService.getAllSuppliers();
}


}