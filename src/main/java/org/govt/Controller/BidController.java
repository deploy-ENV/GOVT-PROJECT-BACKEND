package org.govt.Controller;

import java.util.List;

import org.govt.model.Bid;
import org.govt.model.Project;
import org.govt.service.BidService;
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
@RequestMapping("/bids")
public class BidController {
    @Autowired
private BidService bidService;

// Submit a new bid (Contractor)
@PostMapping
public ResponseEntity<Bid> submitBid(@RequestBody Bid bid) {
    return ResponseEntity.ok(bidService.submitBid(bid));
}

// List all bids for a project (PM)
@GetMapping("/project/{projectId}")
public ResponseEntity<List<Bid>> getBidsForProject(@PathVariable String projectId) {
    return ResponseEntity.ok(bidService.getBidsForProject(projectId));
}
@GetMapping("/project/{contractorId}")
public ResponseEntity<List<Bid>> getBidsContrea(@PathVariable String contractorId) {
    return ResponseEntity.ok(bidService.getBidsForContractor(contractorId));
}


}
