package org.govt.service;

import java.time.LocalDate;
import java.util.List;

import org.govt.model.ProjectProgress;
import org.govt.repository.ProjectProgressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class ProjectProgressService {
    @Autowired private ProjectProgressRepository progressRepo;

public ProjectProgress submitProgress(ProjectProgress progress) {
    progress.setSubmittedAt(LocalDate.now());
    progress.setVerified(false);
    return progressRepo.save(progress);
}

public ProjectProgress verifyProgress(String progressId, String comment) {
    ProjectProgress progress = progressRepo.findById(progressId).orElseThrow();
    progress.setVerified(true);
    progress.setVerificationComment(comment);
    return progressRepo.save(progress);
}

public List<ProjectProgress> getProgressForProject(String projectId) {
    return progressRepo.findByProjectIdOrderBySubmittedAtDesc(projectId);
}

    
}
