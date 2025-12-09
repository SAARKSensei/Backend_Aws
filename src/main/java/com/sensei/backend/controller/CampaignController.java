package com.sensei.backend.controller;

import com.sensei.backend.entity.Campaign;
import com.sensei.backend.service.CampaignService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/campaigns")
@RequiredArgsConstructor
public class CampaignController {

    private final CampaignService campaignService;

    // POST - Create
    @PostMapping
    public ResponseEntity<Campaign> createCampaign(@RequestBody Campaign campaign) {
        return ResponseEntity.ok(campaignService.createCampaign(campaign));
    }

    // GET - All
    @GetMapping
    public ResponseEntity<List<Campaign>> getAllCampaigns() {
        return ResponseEntity.ok(campaignService.getAllCampaigns());
    }
    // GET - Get campaign by ID
    @GetMapping("/{id}")
    public ResponseEntity<Campaign> getCampaignById(@PathVariable String id) {
        Campaign campaign = campaignService.getCampaignById(id);
        return ResponseEntity.ok(campaign);
    }
    // GET - By name
    @GetMapping("/by-name/{name}")
    public ResponseEntity<Campaign> getCampaignByName(@PathVariable String name) {
        return ResponseEntity.ok(campaignService.getCampaignByName(name));
    }

    // PUT - Update
    @PutMapping("/{id}")
    public ResponseEntity<Campaign> updateCampaign(@PathVariable String id, @RequestBody Campaign campaign) {
        return ResponseEntity.ok(campaignService.updateCampaign(id, campaign));
    }
 // DELETE - Delete a campaign
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCampaign(@PathVariable String id) {
        campaignService.deleteCampaign(id);  // call service to delete
        return ResponseEntity.noContent().build();
    }

}
