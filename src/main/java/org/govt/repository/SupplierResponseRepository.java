package org.govt.repository;

import java.util.List;

import org.govt.model.SupplierResponse;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SupplierResponseRepository extends MongoRepository<SupplierResponse, String> {
    List<SupplierResponse> findBySupplierRequestId(String requestId);
}
