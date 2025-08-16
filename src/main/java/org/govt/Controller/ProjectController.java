package org.govt.Controller;

import java.util.List;

import org.govt.model.Project;
import org.govt.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;


    @PostMapping("/pm/{pmId}/dept/{departmentId}/name/{pmName}")
    public ResponseEntity<Project> createProject(@RequestBody Project project,
                                                 @PathVariable String pmId,
                                                 @PathVariable String departmentId,
                                                 @PathVariable String pmName) {
        Project saved = projectService.createProject(project, pmId, departmentId, pmName);
        return ResponseEntity.ok(saved);
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

  
    @PostMapping("/{projectId}/finalize/contractor/{contractorId}/supervisor/{supervisorId}")
    public ResponseEntity<Project> finalizeAssignments(
            @PathVariable String projectId,
            @PathVariable String contractorId,
            @PathVariable String supervisorId) {
        return ResponseEntity.ok(
            projectService.finalizeProjectAssignments(projectId, contractorId, supervisorId)
        );
    }
}
