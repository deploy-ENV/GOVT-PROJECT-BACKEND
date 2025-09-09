package org.govt.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document("fund_transactions")
@Data
public class FundTransaction {
    @Id
    private String id;
  private String fromUserId;
private String toUserId;
private String fromRole;
private String toRole;   
private String projectId;
private BigDecimal amount;
private String purpose;
private String status; 
private String timestamp;

    
}