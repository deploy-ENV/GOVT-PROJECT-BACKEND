package org.govt.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("bids")
public class Bid {
    @Id
    private String id;
    private String projectId;
    private String contractorId;
    private BigDecimal bidAmount;
    private String proposalText;
    private String status; 
    private LocalDate createdAt;
  
}