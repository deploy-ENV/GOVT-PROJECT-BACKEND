package org.govt.repository;

import java.util.List;

import org.govt.model.ProjectProgress;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProjectProgressRepository extends MongoRepository<ProjectProgress, String> {
List<ProjectProgress> findByProjectIdOrderBySubmittedAtDesc(String projectId);
}
