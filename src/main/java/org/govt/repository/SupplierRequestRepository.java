package org.govt.repository;

import java.util.List;

import org.govt.model.SupplierRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SupplierRequestRepository extends MongoRepository<SupplierRequest, String> {
    List<SupplierRequest> findByProjectId(String projectId);
    List<SupplierRequest> findByContractorId(String contractorId);
    List<SupplierRequest> findBySupplierId(String supplierId);
}
