package org.govt.service;

import java.io.InputStream;
import java.time.LocalDate;

// import javax.management.Query;
import org.springframework.data.mongodb.core.query.Query;

import org.bson.types.ObjectId;
import org.govt.model.DocumentUpload;
import org.govt.repository.DocumentUploadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mongodb.client.gridfs.model.GridFSFile;

import java.io.IOException;
import java.util.List;

@Service
public class DocumentUploadService {
    @Autowired private GridFsTemplate gridFsTemplate;
    @Autowired private DocumentUploadRepository docRepo;
public DocumentUpload upload(MultipartFile file, String entityType, String entityId, String purpose, String userId, String role) throws IOException {
    ObjectId fileId = gridFsTemplate.store(file.getInputStream(), file.getOriginalFilename(), file.getContentType());

    DocumentUpload doc = new DocumentUpload();
    doc.setFileId(fileId.toString());
    doc.setFileName(file.getOriginalFilename());
    doc.setFileType(file.getContentType());
    doc.setFileSize(file.getSize());
    doc.setUploadedBy(userId);
    doc.setRole(role);
    doc.setUploadedAt(LocalDate.now().toString());

    doc.setEntityType(entityType);     // generic type like "project", "bid", etc.
    doc.setEntityId(entityId);         // ID of the entity (e.g. project ID)
    doc.setPurpose(purpose);           // Optional: e.g. blueprint, invoice

    return docRepo.save(doc);
}

    public List<DocumentUpload> getDocumentsForEntity(String entityType, String entityId) {
    return docRepo.findByEntityTypeAndEntityId(entityType, entityId);
}


    public InputStream getFileStream(String fileId) throws IOException {
        GridFSFile file = getGridFSFile(fileId);
        return gridFsTemplate.getResource(file).getInputStream();
    }

    public GridFSFile getGridFSFile(String fileId) {
        return gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(new ObjectId(fileId))));
    }

    public List<DocumentUpload> listProjectFiles(String projectId) {
        return docRepo.findByProjectId(projectId);
    }
}
