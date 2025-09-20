package org.govt.Controller;

import java.util.List;

import org.govt.model.SupplierRequest;
import org.govt.model.SupplierResponse;
import org.govt.service.SupplierRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/supplier-requests")
public class SupplierRequestController {
    @Autowired private SupplierRequestService service;

    @PostMapping
    public ResponseEntity<SupplierRequest> create(@RequestBody SupplierRequest req) {
        return ResponseEntity.ok(service.createRequest(req));
    }

    @PostMapping("/{id}/status")
    public ResponseEntity<SupplierRequest> updateStatus(
        @PathVariable String id,
        @RequestParam String status
    ) {
        return ResponseEntity.ok(service.updateStatus(id, status));
    }

    @GetMapping("/supplier/{supplierId}")
    public List<SupplierRequest> forSupplier(@PathVariable String supplierId) {
        return service.getSupplierRequests(supplierId);
    }

    @GetMapping("/contractor/{contractorId}")
    public List<SupplierRequest> forContractor(@PathVariable String contractorId) {
        return service.getContractorRequests(contractorId);
    }
}
