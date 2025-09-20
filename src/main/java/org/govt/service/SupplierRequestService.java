package org.govt.service;

import java.time.LocalDate;
import java.util.List;

import org.govt.model.SupplierRequest;
import org.govt.model.SupplierResponse;
import org.govt.repository.SupplierRequestRepository;
import org.govt.repository.SupplierResponseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SupplierRequestService {
    @Autowired private SupplierRequestRepository requestRepo;

    public SupplierRequest createRequest(SupplierRequest req) {
        req.setStatus("PENDING");
        req.setRequestedAt(LocalDate.now().toString());
        return requestRepo.save(req);
    }

    public SupplierRequest updateStatus(String id, String status) {
        SupplierRequest req = requestRepo.findById(id).orElseThrow();
        req.setStatus(status);
        if ("DELIVERED".equals(status)) {
            req.setFulfilledAt(LocalDate.now().toString());
        }
        return requestRepo.save(req);
    }

    public List<SupplierRequest> getSupplierRequests(String supplierId) {
        return requestRepo.findBySupplierId(supplierId);
    }

    public List<SupplierRequest> getContractorRequests(String contractorId) {
        return requestRepo.findByContractorId(contractorId);
    }
}
