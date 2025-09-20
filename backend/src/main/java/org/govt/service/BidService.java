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
    bid.setSubmittedAt(LocalDate.now());
    bid.setStatus("PENDING");
    return bidRepository.save(bid);
}

// List all bids for a given project
public List<Bid> getBidsForProject(String projectId) {
    return bidRepository.findByProjectId(projectId);
}

// Assign a contractor to a project
public Project assignContractor(String projectId, String contractorId) {
    Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new RuntimeException("Project not found"));

    Bid selectedBid = bidRepository.findByProjectIdAndContractorId(projectId, contractorId)
            .orElseThrow(() -> new RuntimeException("Selected contractor has not bid on this project"));

    // Set accepted for the chosen one
    selectedBid.setStatus("ACCEPTED");
    bidRepository.save(selectedBid);

    // Reject other bids
    List<Bid> allBids = bidRepository.findByProjectId(projectId);
    for (Bid b : allBids) {
        if (!b.getContractorId().equals(contractorId)) {
            b.setStatus("REJECTED");
            bidRepository.save(b);
        }
    }

    // Assign contractor and update project status
    project.setAssignedContractorId(contractorId);
    project.setStatus(ProjectStatus.ASSIGNED);
    return projectRepository.save(project);
}

}
