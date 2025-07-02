package org.govt.service;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.govt.model.FundTransaction;
import org.govt.model.Project;
import org.govt.repository.FundTransactionRepository;
import org.govt.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FundService {
    @Autowired private FundTransactionRepository fundRepo;
    @Autowired private ProjectRepository projectRepo;

   public FundTransaction requestFund(FundTransaction txn) {
txn.setStatus("PENDING");
txn.setTimestamp(LocalDate.now());
return fundRepo.save(txn);
}
    

    public FundTransaction approveFund(String txnId, boolean approve) {
        FundTransaction txn = fundRepo.findById(txnId).orElseThrow();
        txn.setStatus(approve ? "APPROVED" : "REJECTED");
       if (approve) {
    Project project = projectRepo.findById(txn.getProjectId()).orElseThrow();
    BigDecimal current = project.getBudgetApproved();
    project.setBudgetApproved(current.subtract(txn.getAmount()));
    projectRepo.save(project);
}

return fundRepo.save(txn);
    }
}