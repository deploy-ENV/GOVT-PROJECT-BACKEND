package org.govt.repository;

import java.util.List;

import org.govt.model.SupplierPaymentRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SupplierPaymentRequestRepository extends MongoRepository<SupplierPaymentRequest, String> {
    List<SupplierPaymentRequest> findByProjectId(String projectId);
    List<SupplierPaymentRequest> findByStatus(String status);
}
