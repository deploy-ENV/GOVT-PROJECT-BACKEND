package org.govt.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.govt.Enums.ProjectStatus;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
@Data
@Document("projects")
public class Project {
   @Id
private String id;

// Basic Info
private String title;
private String description;
private String departmentId;    // inferred from PM
private Address location;

// PM Info
private String projectManagerId;
private String createdByName;

// Timeline
private String expectedStartDate;
private String deadline;
private String bidSubmissionDeadline;
private String createdAt;

// Budget
private BigDecimal totalBudget;
private BigDecimal budgetApproved ;
private BigDecimal budgetUsed ;

// Contractor Requirements
private String contractorRequirements; // skills, license types, etc.
private List<String> requiredMaterials;
private List<String> estimatedQuantities;

// Document Upload IDs (reference to DocumentUpload.entityId)
private List<String> documentIds; // store GridFS 

// Bidding and Assignment
private String assignedContractorId;
private String assignedSupervisorId;
private List<String> assignedSupplierIds;

// Flags & Status
private boolean aiSupplierMatchEnabled = false;
private ProjectStatus status; // DRAFT, BIDDING, ASSIGNED, IN_PROGRESS, COMPLETED, CLOSED
private String comments;

    private List<ProjectProgress> progressSteps;
private String thumbnailUrl;

}