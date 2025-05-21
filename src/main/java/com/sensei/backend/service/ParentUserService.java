package com.sensei.backend.service;

import com.sensei.backend.dto.ParentUserDTO;
import com.sensei.backend.dto.ChildUserDTO;
import com.sensei.backend.entity.ParentUser;
import com.sensei.backend.entity.ChildUser;
import com.sensei.backend.entity.PricingPlan;
import com.sensei.backend.entity.Subject;
import com.sensei.backend.exception.ResourceNotFoundException;
import com.sensei.backend.repository.ParentUserRepository;
import com.sensei.backend.repository.PricingPlanRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.Optional;
import java.util.Objects;
import java.util.Calendar;
import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class ParentUserService {

    private final ParentUserRepository parentUserRepository;
    private final PricingPlanRepository pricingPlanRepository;
    private final ModelMapper modelMapper;

    public ParentUserDTO createParentUser(ParentUserDTO parentUserDTO) {
        ParentUser parentUser = modelMapper.map(parentUserDTO, ParentUser.class);
        parentUser = parentUserRepository.save(parentUser);
        log.info("Created ParentUser with ID: {}", parentUser.getParentId());
        return modelMapper.map(parentUser, ParentUserDTO.class);
    }

    public ParentUserDTO getParentUserById(String parentId) {
        ParentUser parentUser = parentUserRepository.findById(parentId)
                .orElseThrow(() -> new ResourceNotFoundException("ParentUser not found with ID: " + parentId));
        return modelMapper.map(parentUser, ParentUserDTO.class);
    }

    public List<ParentUserDTO> getAllParentUsers() {
        List<ParentUser> parentUsers = parentUserRepository.findAll();
        return parentUsers.stream()
                .map(parentUser -> modelMapper.map(parentUser, ParentUserDTO.class))
                .collect(Collectors.toList());
    }

    public ParentUserDTO updateParentUser(String parentId, ParentUserDTO parentUserDTO) {
        ParentUser existingParentUser = parentUserRepository.findById(parentId)
                .orElseThrow(() -> new ResourceNotFoundException("ParentUser not found with ID: " + parentId));

        modelMapper.map(parentUserDTO, existingParentUser);
        existingParentUser = parentUserRepository.save(existingParentUser);
        log.info("Updated ParentUser with ID: {}", existingParentUser.getParentId());
        return modelMapper.map(existingParentUser, ParentUserDTO.class);
    }

    public void deleteParentUser(String parentId) {
        ParentUser parentUser = parentUserRepository.findById(parentId)
                .orElseThrow(() -> new ResourceNotFoundException("ParentUser not found with ID: " + parentId));
        parentUserRepository.delete(parentUser);
        log.info("Deleted ParentUser with ID: {}", parentId);
    }

    public ParentUserDTO getParentUserByUserName(String userName) {
        ParentUser parentUser = parentUserRepository.findByUserName(userName)
                .orElseThrow(() -> new ResourceNotFoundException("ParentUser not found with username: " + userName));
        return modelMapper.map(parentUser, ParentUserDTO.class);
    }

    // get parent user by phone number
//    public ParentUserDTO getParentUserByPhoneNumber(String phone) {
//        ParentUser parentUser = parentUserRepository.findByPhoneNumberWithChildUsers(phone)
//                .orElseThrow(() -> new ResourceNotFoundException("ParentUser not found with phoneNumber: " + phone));
//        return modelMapper.map(parentUser, ParentUserDTO.class);
//    }

    // get parent user with child user by phone number
    public ParentUserDTO getParentUserByPhoneNumber(String phone) {
        ParentUser parentUser = parentUserRepository.findByPhoneNumberWithChildUsers(phone)
                .orElseThrow(() -> new ResourceNotFoundException("ParentUser not found with phoneNumber: " + phone));

        ParentUserDTO parentUserDTO = modelMapper.map(parentUser, ParentUserDTO.class);

        // Ensure children mapping is correct if needed
        if (parentUser.getChildUsers() != null) {
            List<ChildUserDTO> childUserDTOList = parentUser.getChildUsers().stream()
                    .map(child -> modelMapper.map(child, ChildUserDTO.class))
                    .collect(Collectors.toList());
            parentUserDTO.setChildUsers(childUserDTOList);
        }

        return parentUserDTO;
    }

    //Get Parent User By Email
    public Optional<ParentUser> findByEmail(String email) {
        return parentUserRepository.findByEmail(email);
    }

    // get Pricing plan of child user associated to Parent User
    public Map<String, Object> getPricingPlanForParent(String email) {
        Optional<ParentUser> optionalParentUser = parentUserRepository.findByEmail(email);

        if (!optionalParentUser.isPresent()) {
            return null; // No parent found
        }

        ParentUser parentUser = optionalParentUser.get(); // Extract ParentUser from Optional

        for (ChildUser child : parentUser.getChildUsers()) {
            if (child.getActivePlanId() != null) {
                Optional<PricingPlan> optionalPlan = pricingPlanRepository.findById(child.getActivePlanId());

                if (optionalPlan.isPresent()) {
                    PricingPlan pricingPlan = optionalPlan.get();

                    // Fetch start date
                    Date planStartDate = child.getPlanStartDate();
                    if (planStartDate == null) {
                        return null; // No start date, invalid plan
                    }

                    int durationMonths = pricingPlan.getDurationInMonths();

                    // Calculate expiry date
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(planStartDate);
                    calendar.add(Calendar.MONTH, durationMonths);
                    Date expiryDate = calendar.getTime();

                    // Check if plan is active
                    boolean isPlanActive = new Date().before(expiryDate);

                    if (!isPlanActive) {
                        return null; // Plan is expired, return null
                    }

                    // Build response only if plan is active
                    Map<String, Object> response = new HashMap<>();
                    response.put("childId", child.getChildId());
                    response.put("childName", child.getChildName());
                    response.put("pricingPlan", pricingPlan);
                    response.put("planStartDate", planStartDate);
                    response.put("expiryDate", expiryDate);
                    response.put("isPlanActive", true);

                    return response;
                }
            }
        }

        return null; // No active plan found
    }

}
