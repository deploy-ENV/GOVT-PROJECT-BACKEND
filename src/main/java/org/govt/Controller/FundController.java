package org.govt.Controller;

import org.govt.model.FundTransaction;
import org.govt.service.FundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/funds")
public class FundController {
    @Autowired private FundService service;

    @PostMapping("/request")
    public ResponseEntity<FundTransaction> requestFund(@RequestBody FundTransaction txn) {
        return ResponseEntity.ok(service.requestFund(txn));
    }

    @PostMapping("/approve")
    public ResponseEntity<FundTransaction> approve(@RequestParam String txnId, @RequestParam boolean approve) {
        return ResponseEntity.ok(service.approveFund(txnId, approve));
    }
}
