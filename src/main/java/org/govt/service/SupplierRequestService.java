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
    @Autowired private SupplierResponseRepository responseRepo;

    public SupplierRequest createRequest(SupplierRequest req) {
        req.setStatus("REQUESTED");
        req.setCreatedAt(LocalDate.now());
        return requestRepo.save(req);
    }

    public List<SupplierRequest> getRequestsBySupplier(String supplierId) {
        return requestRepo.findBySupplierId(supplierId);
    }

    public SupplierResponse respond(String requestId, SupplierResponse response) {
        response.setSupplierRequestId(requestId);
        response.setResponseDate(LocalDate.now());
        SupplierRequest req = requestRepo.findById(requestId).orElseThrow();
        req.setStatus("SENT");
        requestRepo.save(req);
        return responseRepo.save(response);
    }
}

