package com.sensei.backend.service;

import com.sensei.backend.entity.Campaign;
import com.sensei.backend.repository.CampaignRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CampaignService {

    private final CampaignRepository campaignRepository;

    // Create / Save
    public Campaign createCampaign(Campaign campaign) {
        return campaignRepository.save(campaign);
    }

    // Get All
    public List<Campaign> getAllCampaigns() {
        return campaignRepository.findAll();
    }
    public Campaign getCampaignById(String id) {
        return campaignRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Campaign not found with id: " + id));
    }
    public Campaign getCampaignByName(String name) {
        return campaignRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Campaign not found with name: " + name));
    }

    // Update
    public Campaign updateCampaign(String id, Campaign updatedCampaign) {
        Campaign campaign = campaignRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Campaign not found with id: " + id));

        campaign.setSchoolName(updatedCampaign.getSchoolName());
        campaign.setEmail(updatedCampaign.getEmail());
        campaign.setPhone(updatedCampaign.getPhone());
        campaign.setTime(updatedCampaign.getTime());

        return campaignRepository.save(campaign);
    }
 // Delete a campaign by ID
    public void deleteCampaign(String id) {
        Campaign campaign = campaignRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Campaign not found with id: " + id));
        campaignRepository.delete(campaign);
    }

}
