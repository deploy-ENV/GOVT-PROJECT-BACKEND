package org.govt.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document("bids")
@Data
public class Bid {
    @Id
   private String projectId;
private String contractorId;
private String contractorName;

private BigDecimal bidAmount;
private String timelineEstimate;
private String materialsPlan;
private String proposalText;

private int experienceMatchPercent;
private String status; // PENDING, ACCEPTED, REJECTED

private LocalDate submittedAt;

  
}