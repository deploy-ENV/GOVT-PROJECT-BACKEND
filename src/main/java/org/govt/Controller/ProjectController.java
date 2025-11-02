package org.govt.Controller;

import java.util.List;

import org.govt.Enums.ProjectStatus;
import org.govt.model.Address;
import org.govt.model.Project;
import org.govt.model.User_Supervisor;
import org.govt.model.User_Supplier;
import org.govt.service.ProjectService;
import org.govt.service.UserSupervisorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.aggregation.ArithmeticOperators.Add;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.method.P;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;
    @Autowired
    private UserSupervisorService user_Supervisor;


    @PostMapping("/pm/{pmId}/dept/{departmentId}/name/{pmName}")
    public ResponseEntity<Project> createProject(@RequestBody Project project,
                                                 @PathVariable String pmId,
                                                 @PathVariable String departmentId,
                                                 @PathVariable String pmName) {
        Project saved = projectService.createProject(project, pmId, departmentId, pmName);
        return ResponseEntity.ok(saved);
    }
    @GetMapping("/all")
    public ResponseEntity<List<Project>> getAllProjects() {
        List<Project> projects = projectService.listAllProjects();
        return ResponseEntity.ok(projects);
    }


    @GetMapping("/pm/{pmId}")
    public ResponseEntity<List<Project>> getMyProjects(@PathVariable String pmId) {
        List<Project> projects = projectService.listMyProjects(pmId);
        return ResponseEntity.ok(projects);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Project> getById(@PathVariable String id) {
        return ResponseEntity.ok(projectService.getById(id));
    }

  
   @PostMapping("/{projectId}/finalize/contractor/{contractorId}/supervisor/{supervisorId}/suppliers")
public ResponseEntity<Project> finalizeAssignments(
        @PathVariable String projectId,
        @PathVariable String contractorId,
        @PathVariable String supervisorId,
        @RequestBody List<String> supplierIds) {

    return ResponseEntity.ok(
            projectService.finalizeProjectAssignments(projectId, contractorId, supervisorId, supplierIds)
    );
}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable String id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("/all")
    public ResponseEntity<Void> deleteAllProjects() {
        projectService.deleteAllProjects();
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/{id}/status/{status}/{index}")
    public ResponseEntity<Project> updateProjectStatus(@PathVariable String id, @PathVariable String status, @PathVariable int index) {
        Project project = projectService.getById(id);
        
        Project updatedProject = projectService.updateProjectProgress(id, index, status);
        return ResponseEntity.ok(updatedProject);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Project> updateProject(@PathVariable String id, @RequestBody Project updatedProject) {
        Project existingProject = projectService.getById(id);
        
        // Update fields
        existingProject.setTitle(updatedProject.getTitle());
        existingProject.setDescription(updatedProject.getDescription());
        existingProject.setLocation(updatedProject.getLocation());
        existingProject.setExpectedStartDate(updatedProject.getExpectedStartDate());
        existingProject.setDeadline(updatedProject.getDeadline());
        existingProject.setBidSubmissionDeadline(updatedProject.getBidSubmissionDeadline());
        existingProject.setTotalBudget(updatedProject.getTotalBudget());
        existingProject.setBudgetApproved(updatedProject.getBudgetApproved());
        existingProject.setBudgetUsed(updatedProject.getBudgetUsed());
        existingProject.setContractorRequirements(updatedProject.getContractorRequirements());
        existingProject.setRequiredMaterials(updatedProject.getRequiredMaterials());
        existingProject.setEstimatedQuantities(updatedProject.getEstimatedQuantities());
        existingProject.setDocumentIds(updatedProject.getDocumentIds());
        existingProject.setAiSupplierMatchEnabled(updatedProject.isAiSupplierMatchEnabled());
        existingProject.setComments(updatedProject.getComments());
        existingProject.setThumbnailUrl(updatedProject.getThumbnailUrl());
        
        Project savedProject = projectService.updateProject(updatedProject);
        return ResponseEntity.ok(savedProject);
    }
     @PostMapping("/supervisors/nearest")
    public List<User_Supervisor> getNearestSupervisor(
            @RequestBody Address address) {
        return user_Supervisor.findNearestSupervisor( address);
    }
    @PostMapping("/suplier/nearest")
    public List<User_Supplier> getNearestSupplier(
           @RequestBody Address address) {
        return projectService.findNearestSupplier(address);
    }
    

}
