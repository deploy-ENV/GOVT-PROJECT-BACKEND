package org.govt.service;

import java.time.LocalDate;
import java.util.List;

import org.govt.Enums.ProjectStatus;
import org.govt.model.Bid;
import org.govt.model.Project;
import org.govt.repository.BidRepository;
import org.govt.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class BidService {
    @Autowired
private BidRepository bidRepository;

@Autowired
private ProjectRepository projectRepository;

// Submit a bid
public Bid submitBid(Bid bid) {
    // Prevent duplicate bids by same contractor
    if (bidRepository.findByProjectIdAndContractorId(bid.getProjectId(), bid.getContractorId()).isPresent()) {
        throw new RuntimeException("You already submitted a bid for this project.");
    }
    bid.setSubmittedAt(LocalDate.now().toString());
    bid.setStatus("PENDING");
    return bidRepository.save(bid);
}

// List all bids for a given project
public List<Bid> getBidsForProject(String projectId) {
    return bidRepository.findByProjectId(projectId);
}

    public Bid acceptContractorBid(String projectId, String contractorId) {
        Bid selectedBid = bidRepository.findByProjectIdAndContractorId(projectId, contractorId)
                .orElseThrow(() -> new RuntimeException("Selected contractor has not bid on this project"));

        // Accept chosen bid
        selectedBid.setStatus("ACCEPTED");
        bidRepository.save(selectedBid);

        // Reject others
        List<Bid> allBids = bidRepository.findByProjectId(projectId);
        for (Bid b : allBids) {
            if (!b.getContractorId().equals(contractorId)) {
                b.setStatus("REJECTED");
                bidRepository.save(b);
            }
        }
        return selectedBid;
    }


}
