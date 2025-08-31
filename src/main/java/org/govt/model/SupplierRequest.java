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
    private String projectId;
    private String contractorId;
    private String supplierId;
    private String itemName;
    private String itemDescription;
    private int quantity;
    private String status; // PENDING, APPROVED, REJECTED, DELIVERED
    private String requestedAt;
    private String fulfilledAt;
}

