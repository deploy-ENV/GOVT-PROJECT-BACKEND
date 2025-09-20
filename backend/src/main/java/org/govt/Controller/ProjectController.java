package org.govt.Controller;

import java.util.List;

import org.govt.model.Project;
import org.govt.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/projects")
public class ProjectController {
   @Autowired
private ProjectService projectService;

// Create a project (by PM)
@PostMapping
public ResponseEntity<Project> createProject(@RequestBody Project project,
                                             @RequestParam String pmId,
                                             @RequestParam String departmentId,
                                             @RequestParam String pmName) {
    Project saved = projectService.createProject(project, pmId, departmentId, pmName);
    return ResponseEntity.ok(saved);
}

// Get all projects posted by this PM
@GetMapping("/mine")
public ResponseEntity<List<Project>> getMyProjects(@RequestParam String pmId) {
    List<Project> projects = projectService.listMyProjects(pmId);
    return ResponseEntity.ok(projects);
}

// (Optional) Get project by ID
@GetMapping("/{id}")
public ResponseEntity<Project> getById(@PathVariable String id) {
    return ResponseEntity.ok(projectService.getById(id));
}
@PostMapping("/{projectId}/finalize")
public ResponseEntity<Project> finalizeAssignments(
@PathVariable String projectId,
@RequestParam String contractorId,
@RequestParam String supervisorId
) {
return ResponseEntity.ok(projectService.finalizeProjectAssignments(projectId, contractorId, supervisorId));
}

}
