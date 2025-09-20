package org.govt.repository;

import java.util.List;
import java.util.Optional;

import org.govt.model.Bid;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BidRepository extends MongoRepository<Bid, String> {
  List<Bid> findByProjectId(String projectId);
Optional<Bid> findByProjectIdAndContractorId(String projectId, String contractorId);
List<Bid> findByContractorId(String contractorId);}