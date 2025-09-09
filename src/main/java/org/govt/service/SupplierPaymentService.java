package org.govt.service;

import java.time.LocalDate;

import org.govt.model.FundTransaction;
import org.govt.model.Project;
import org.govt.model.SupplierPaymentRequest;
import org.govt.repository.FundTransactionRepository;
import org.govt.repository.ProjectRepository;
import org.govt.repository.SupplierPaymentRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SupplierPaymentService {
    @Autowired private SupplierPaymentRequestRepository repo;
    @Autowired private FundTransactionRepository fundRepo;
    @Autowired private ProjectRepository projectRepo;

    public SupplierPaymentRequest requestPayment(SupplierPaymentRequest req) {
        req.setStatus("PENDING");
        req.setRequestedAt(LocalDate.now().toString());
        return repo.save(req);
    }

    public SupplierPaymentRequest verifyRequest(String requestId, String supervisorId) {
        SupplierPaymentRequest req = repo.findById(requestId).orElseThrow();
        req.setStatus("VERIFIED");
        req.setVerifiedBy(supervisorId);
        req.setVerifiedAt(LocalDate.now().toString());
        return repo.save(req);
    }

    public SupplierPaymentRequest approveRequest(String requestId, String approverId) {
        SupplierPaymentRequest req = repo.findById(requestId).orElseThrow();
        if (!"VERIFIED".equals(req.getStatus())) {
            throw new IllegalStateException("Request must be verified before approval");
        }

        Project p = projectRepo.findById(req.getProjectId()).orElseThrow();
        if (p.getBudgetApproved().compareTo(java.math.BigDecimal.valueOf(req.getAmount())) < 0) {
            throw new IllegalStateException("Insufficient budget");
        }

        // Deduct from project budget
        p.setBudgetApproved(p.getBudgetApproved().subtract(java.math.BigDecimal.valueOf(req.getAmount())));
        projectRepo.save(p);

        // Create FundTransaction
        FundTransaction txn = new FundTransaction();
        txn.setFromUserId(p.getProjectManagerId());
        txn.setToUserId(req.getSupplierId());
        txn.setFromRole("PM");
        txn.setToRole("SUPPLIER");
        txn.setProjectId(p.getId());
        txn.setAmount(java.math.BigDecimal.valueOf(req.getAmount()));
        txn.setPurpose("SUPPLIER_PAYMENT");
        txn.setStatus("APPROVED");
        txn.setTimestamp(LocalDate.now().toString());
        fundRepo.save(txn);

        // Update payment request
        req.setStatus("PAID");
        req.setApprovedBy(approverId);
        req.setApprovedAt(LocalDate.now().toString());
        req.setPaidAt(LocalDate.now().toString());

        return repo.save(req);
    }
}
