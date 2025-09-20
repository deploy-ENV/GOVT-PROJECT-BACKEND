package org.govt.service;

import java.time.LocalDate;
import java.util.List;

import org.govt.Enums.ProjectStatus;
import org.govt.model.Bid;
import org.govt.model.Project;
import org.govt.model.User_Supplier;
import org.govt.repository.BidRepository;
import org.govt.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {
  @Autowired
private ProjectRepository projectRepository;
@Autowired
private BidRepository bidRepository;
@Autowired
private UserSupplierService supplierService; 

// Create a new project
public Project createProject(Project project, String pmId, String departmentId, String pmName) {
    project.setProjectManagerId(pmId);
    project.setDepartmentId(departmentId);
    project.setCreatedByName(pmName);
    project.setCreatedAt(LocalDate.now());
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
public Project finalizeProjectAssignments(String projectId, String contractorId, String supervisorId) {
Project project = projectRepository.findById(projectId)
.orElseThrow(() -> new RuntimeException("Project not found"));
// Accept selected contractor
Bid selectedBid = bidRepository.findByProjectIdAndContractorId(projectId, contractorId)
    .orElseThrow(() -> new RuntimeException("Selected contractor has not bid"));
selectedBid.setStatus("ACCEPTED");
bidRepository.save(selectedBid);

// Reject all others
List<Bid> allBids = bidRepository.findByProjectId(projectId);
for (Bid b : allBids) {
    if (!b.getContractorId().equals(contractorId)) {
        b.setStatus("REJECTED");
        bidRepository.save(b);
    }
}

// Auto-fetch suppliers
List<User_Supplier> matchedSuppliers = supplierService.autoFetchSuppliers(projectId);
List<String> supplierIds = matchedSuppliers.stream()
    .map(User_Supplier::getId)
    .toList();

// Assign all to project
project.setAssignedContractorId(contractorId);
project.setAssignedSupervisorId(supervisorId);
project.setAssignedSupplierIds(supplierIds);
project.setStatus(ProjectStatus.IN_PROGRESS); // or ASSIGNED
return projectRepository.save(project);

}
}