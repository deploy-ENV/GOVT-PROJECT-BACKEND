package org.govt.model;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
 @Document("project_progress")
@Data
public class ProjectProgress {

    private String id;
    private String title;
    private String description;

    private String supervisorId;

    // NOT_STARTED, IN_PROGRESS, COMPLETED, LOCKED
    private String status;     

    private LocalDate dueDate;
    private LocalDate startedAt;
    private LocalDate completedAt;

    private int order;
}
