package org.govt.Controller;

import java.util.List;

import org.govt.model.SupplierRequest;
import org.govt.model.SupplierResponse;
import org.govt.service.SupplierRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/supplier-requests")
public class SupplierInteractionController {
    @Autowired private SupplierRequestService service;

    @PostMapping
    public ResponseEntity<SupplierRequest> create(@RequestBody SupplierRequest req) {
        return ResponseEntity.ok(service.createRequest(req));
    }

    @GetMapping
    public List<SupplierRequest> getRequests(@RequestParam String supplierId) {
        return service.getRequestsBySupplier(supplierId);
    }

    @PostMapping("/{id}/respond")
    public ResponseEntity<SupplierResponse> respond(
            @PathVariable String id,
            @RequestBody SupplierResponse response
    ) {
        return ResponseEntity.ok(service.respond(id, response));
    }
}

