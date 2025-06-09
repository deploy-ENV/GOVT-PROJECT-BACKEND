package org.govt.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
@Data
@Document("projects")
public class Project {
    @Id
    private String id;
    private String title;
    private String description;
    private String departmentId;
    private String projectManagerId;
    private String assignedContractorId;
    private String supervisorId;
    private List<String> assignedSupplierIds;
    private String status; 
    private BigDecimal budgetAllocated;
    private BigDecimal budgetApproved;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate createdAt;
}