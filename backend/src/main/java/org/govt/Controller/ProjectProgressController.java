package org.govt.Controller;

import java.util.List;

import org.govt.model.ProjectProgress;
import org.govt.service.ProjectProgressService;
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
@RequestMapping("/progress")
public class ProjectProgressController {
    @Autowired private ProjectProgressService service;

@PostMapping("/submit")
public ResponseEntity<ProjectProgress> submit(@RequestBody ProjectProgress progress) {
    return ResponseEntity.ok(service.submitProgress(progress));
}

@PostMapping("/verify")
public ResponseEntity<ProjectProgress> verify(
    @RequestParam String progressId,
    @RequestParam String comment
) {
    return ResponseEntity.ok(service.verifyProgress(progressId, comment));
}

@GetMapping("/project/{projectId}")
public List<ProjectProgress> getProgress(@PathVariable String projectId) {
    return service.getProgressForProject(projectId);
}

}
