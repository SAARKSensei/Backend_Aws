package com.sensei.backend.domain.service;

import com.sensei.backend.adapter.persistence.jpa.entity.ChildUser;
import com.sensei.backend.adapter.persistence.jpa.entity.ParentUser;
import com.sensei.backend.adapter.persistence.jpa.entity.PricingPlan;
import com.sensei.backend.application.dto.ParentUserDTO;
import com.sensei.backend.domain.port.repository.ParentUserRepository;
import com.sensei.backend.domain.port.repository.PricingPlanRepository;
import com.sensei.backend.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ParentUserService {

    private final ParentUserRepository parentUserRepository;
    private final PricingPlanRepository pricingPlanRepository;
    private final ModelMapper modelMapper;

    // ================= CREATE =================
    public ParentUserDTO createParentUser(ParentUserDTO dto) {
        ParentUser parent = modelMapper.map(dto, ParentUser.class);
        parent = parentUserRepository.save(parent);
        return modelMapper.map(parent, ParentUserDTO.class);
    }

    // ================= READ =================
    public ParentUserDTO getParentUserById(UUID parentId) {
        ParentUser parent = parentUserRepository.findById(parentId)
                .orElseThrow(() -> new ResourceNotFoundException("ParentUser not found"));
        return modelMapper.map(parent, ParentUserDTO.class);
    }

    public List<ParentUserDTO> getAllParentUsers() {
        return parentUserRepository.findAll()
                .stream()
                .map(p -> modelMapper.map(p, ParentUserDTO.class))
                .collect(Collectors.toList());
    }

    // ================= UPDATE =================
    public ParentUserDTO updateParentUser(UUID parentId, ParentUserDTO dto) {

    ParentUser parent = parentUserRepository.findById(parentId)
            .orElseThrow(() -> new ResourceNotFoundException("ParentUser not found"));

    // ⚠️ DO NOT TOUCH parentId

    parent.setName(dto.getName());
    parent.setUserName(dto.getUserName());
    parent.setEmail(dto.getEmail());
    parent.setPhone(dto.getPhone());
    parent.setPassword(dto.getPassword());
    parent.setMaritalStatus(dto.getMaritalStatus());
    parent.setOccupation(dto.getOccupation());
    parent.setRelationWithChildren(dto.getRelationWithChildren());

    parent.setSpouseName(dto.getSpouseName());
    parent.setSpouseGender(dto.getSpouseGender());
    parent.setSpouseEmail(dto.getSpouseEmail());
    parent.setSpousePhone(dto.getSpousePhone());
    parent.setSpouseOccupation(dto.getSpouseOccupation());
    parent.setSpouseRelationWithChild(dto.getSpouseRelationWithChild());

    ParentUser saved = parentUserRepository.save(parent);

    return modelMapper.map(saved, ParentUserDTO.class);
}


    // ================= DELETE =================
    public void deleteParentUser(UUID parentId) {
        ParentUser parent = parentUserRepository.findById(parentId)
                .orElseThrow(() -> new ResourceNotFoundException("ParentUser not found"));
        parentUserRepository.delete(parent);
    }

    // ================= FILTERS =================
    public ParentUserDTO getParentUserByUserName(String userName) {
        ParentUser parent = parentUserRepository.findByUserName(userName)
                .orElseThrow(() -> new ResourceNotFoundException("ParentUser not found"));
        return modelMapper.map(parent, ParentUserDTO.class);
    }

    public ParentUserDTO getParentUserByPhoneNumber(String phone) {
        ParentUser parent = parentUserRepository.findByPhoneNumberWithChildUsers(phone)
                .orElseThrow(() -> new ResourceNotFoundException("ParentUser not found"));
        return modelMapper.map(parent, ParentUserDTO.class);
    }

    public Optional<ParentUser> findByEmail(String email) {
        return parentUserRepository.findByEmail(email);
    }

    // ================= PRICING LOOKUP =================
    public Map<String, Object> getPricingPlanForParent(String email) {

        ParentUser parent = parentUserRepository.findByEmail(email)
                .orElse(null);

        if (parent == null || parent.getChildUsers() == null) return null;

        for (ChildUser child : parent.getChildUsers()) {

            // 🔑 New billing model — UUID based
            if (child.getActivePlanId() == null) continue;

            PricingPlan plan = pricingPlanRepository.findById(child.getActivePlanId())
                    .orElse(null);

            if (plan == null) continue;

            Date start = child.getPlanStartDate();
            if (start == null) continue;

            Calendar cal = Calendar.getInstance();
            cal.setTime(start);
            cal.add(Calendar.MONTH, plan.getDurationInMonths());
            Date expiry = cal.getTime();

            if (new Date().after(expiry)) continue;

            Map<String, Object> response = new HashMap<>();
            response.put("childId", child.getChildId());
            response.put("childName", child.getChildName());
            response.put("pricingPlan", plan);
            response.put("planStartDate", start);
            response.put("expiryDate", expiry);
            response.put("isPlanActive", true);

            return response;
        }

        return null;
    }
}
