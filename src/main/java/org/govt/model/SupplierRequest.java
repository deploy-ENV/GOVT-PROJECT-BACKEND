package org.govt.model;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document("supplier_requests")
@Data
public class SupplierRequest {
    @Id
    private String id;
    private String contractorId;
    private String supplierId;
    private String projectId;
    private String materialName;
    private String quantity;
    private String status; 
    private LocalDate createdAt;
}

