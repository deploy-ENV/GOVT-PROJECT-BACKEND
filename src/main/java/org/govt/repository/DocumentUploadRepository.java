package org.govt.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import org.govt.model.DocumentUpload;

public interface DocumentUploadRepository extends MongoRepository<DocumentUpload, String> {
    List<DocumentUpload> findByProjectId(String projectId);
    List<DocumentUpload> findByEntityTypeAndEntityId(String entityType, String entityId);

}
