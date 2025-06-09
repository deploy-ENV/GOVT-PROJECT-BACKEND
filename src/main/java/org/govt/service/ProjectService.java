package org.govt.service;

import java.util.List;

import org.govt.model.Project;
import org.govt.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {
    @Autowired private ProjectRepository projectRepo;
    
    public Project createProject(Project project) {
        project.setStatus("BIDDING");
        return projectRepo.save(project);
    }
    public List<Project> listProjects() {
        return projectRepo.findAll();
    }
    public Project assignContractor(String projectId, String contractorId) {
        Project p = projectRepo.findById(projectId).orElseThrow();
        p.setAssignedContractorId(contractorId);
        p.setStatus("ASSIGNED");
        return projectRepo.save(p);
    }
}