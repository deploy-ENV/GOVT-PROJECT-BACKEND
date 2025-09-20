package org.govt.Controller;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;

import org.bson.types.ObjectId;
import org.govt.model.DocumentUpload;
import org.govt.service.DocumentUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StreamUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mongodb.client.gridfs.model.GridFSFile;

import io.jsonwebtoken.io.IOException;
import jakarta.servlet.http.HttpServletResponse;



@RestController
@RequestMapping("/documents")
public class DocumentUploadController {

    @Autowired
    private DocumentUploadService service;
@PostMapping("/upload")
public ResponseEntity<DocumentUpload> upload(
        @RequestParam("file") MultipartFile file,
        @RequestParam("entityType") String entityType,
        @RequestParam("entityId") String entityId,
        @RequestParam("purpose") String purpose,
        @RequestParam("userId") String userId,
        @RequestParam("role") String role
) throws IOException, java.io.IOException {
    return ResponseEntity.ok(service.upload(file, entityType, entityId, purpose, userId, role));
}


    @GetMapping("/download/{fileId}")
    public void download(@PathVariable String fileId, HttpServletResponse response) throws IOException, java.io.IOException {
        GridFSFile file = service.getGridFSFile(fileId);
        InputStream inputStream = service.getFileStream(fileId);

        response.setContentType(file.getMetadata().getString("_contentType"));
        response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getFilename() + "\"");
        StreamUtils.copy(inputStream, response.getOutputStream());
    }

    @GetMapping("/{entityType}/{entityId}")
    public List<DocumentUpload> getAllByEntity(@PathVariable String entityType, @PathVariable String entityId) {
        return service.getDocumentsForEntity(entityType, entityId);
    }
}
