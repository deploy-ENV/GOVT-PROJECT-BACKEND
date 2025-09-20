package org.govt.Controller;

import org.govt.model.SupplierPaymentRequest;
import org.govt.service.SupplierPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/supplier-payments")
public class SupplierPaymentController {
    @Autowired private SupplierPaymentService service;

    @PostMapping("/request")
    public ResponseEntity<SupplierPaymentRequest> request(@RequestBody SupplierPaymentRequest req) {
        return ResponseEntity.ok(service.requestPayment(req));
    }

    @PostMapping("/verify/{id}")
    public ResponseEntity<SupplierPaymentRequest> verify(@PathVariable String id, @RequestParam String supervisorId) {
        return ResponseEntity.ok(service.verifyRequest(id, supervisorId));
    }

    @PostMapping("/approve/{id}")
    public ResponseEntity<SupplierPaymentRequest> approve(@PathVariable String id, @RequestParam String approverId) {
        return ResponseEntity.ok(service.approveRequest(id, approverId));
    }
}
