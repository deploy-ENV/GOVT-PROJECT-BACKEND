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
    @Autowired private ProjectService service;

    @PostMapping
    public ResponseEntity<Project> create(@RequestBody Project p) {
        return ResponseEntity.ok(service.createProject(p));
    }

    @GetMapping
    public List<Project> getAll() {
        return service.listProjects();
    }

    @PostMapping("/{id}/assign")
    public ResponseEntity<Project> assign(@PathVariable String id, @RequestParam String contractorId) {
        return ResponseEntity.ok(service.assignContractor(id, contractorId));
    }
}
