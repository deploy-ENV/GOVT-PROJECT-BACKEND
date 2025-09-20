package org.govt.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;

import lombok.Data;

@Document("project_documents")
@Data
public class DocumentUpload {
    @Id
    private String id;
  private String projectId;
    private String entityType;     // e.g., "project", "user", "bid", "fund_transaction"
    private String entityId;       // ID of the associated record
    private String purpose;        // e.g., "invoice", "blueprint", "id_proof"

    private String uploadedBy;
    private String role;           // Who uploaded: PM, CONTRACTOR, SUPPLIER

    private String fileName;
    private String fileType;
    private long fileSize;

    private LocalDate uploadedAt;
    private String fileId; // GridFS file ID
}

