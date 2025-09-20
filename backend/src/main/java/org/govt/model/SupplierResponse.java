package org.govt.model;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document("supplier_responses")
@Data
public class SupplierResponse {
    @Id
    private String id;
    private String supplierRequestId;
    private String responseNote;
    private String deliveryStatus; // PENDING, DELIVERED
    private LocalDate responseDate;
}
