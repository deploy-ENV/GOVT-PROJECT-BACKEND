package org.govt.model;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
  @Document("project_progress")
@Data
public class ProjectProgress {
  
@Id
private String id;
private String projectId;
private String submittedByUserId;
private String role; // CONTRACTOR / SUPERVISOR

private String updateText;
private List<String> imageFileIds;  // GridFS file IDs
private int progressPercent; // e.g., 40%

private LocalDate submittedAt;
private boolean verified;   // marked by supervisor
private String verificationComment;

}
