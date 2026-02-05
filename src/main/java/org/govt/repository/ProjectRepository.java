package org.govt.repository;

import java.util.List;
import java.util.Optional;

import org.govt.Enums.ProjectStatus;
import org.govt.model.Bid;
import org.govt.model.FundTransaction;
import org.govt.model.Project;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProjectRepository extends MongoRepository<Project, String> {
  List<Project> findByProjectManagerId(String projectManagerId);

  List<Project> findByStatus(ProjectStatus status);

  List<Project> findByDepartmentId(String departmentId);

  List<Project> findByAssignedSupervisorId(String supervisorId);

  List<Project> findAllByAssignedSupplierIdsContaining(String supplierId);

  List<Project> findByAssignedContractorId(String contractorId);

}
