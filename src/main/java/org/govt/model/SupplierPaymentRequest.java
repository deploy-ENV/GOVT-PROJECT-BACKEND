package org.govt.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Document("supplier_payments")
@Data
public class SupplierPaymentRequest {
    @Id
    private String id;

    private String projectId;
    private String supplierId;
    private String contractorId;
    private String itemDescription;
    private int amount;
    private String status; // PENDING, VERIFIED, APPROVED, REJECTED, PAID
    private String requestedBy; // contractorId
    private String verifiedBy;  // supervisorId
    private String approvedBy;  // PM or DeptAdmin
    private String requestedAt;
    private String verifiedAt;
    private String approvedAt;
    private String paidAt;
}
