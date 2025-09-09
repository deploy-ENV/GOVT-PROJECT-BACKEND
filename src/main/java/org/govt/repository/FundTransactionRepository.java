package org.govt.repository;

import java.util.List;

import org.govt.model.FundTransaction;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FundTransactionRepository extends MongoRepository<FundTransaction, String> {
    List<FundTransaction> findByProjectId(String projectId);
    List<FundTransaction> findByToUserId(String toUserId);
    List<FundTransaction> findByStatus(String status);
}