// package com.sensei.backend.service;

// import com.sensei.backend.dto.ChildUserDTO;
// import com.sensei.backend.entity.ChildUser;
// import com.sensei.backend.exception.ResourceNotFoundException;
// import com.sensei.backend.repository.ChildUserRepository;
// import lombok.RequiredArgsConstructor;
// import lombok.extern.slf4j.Slf4j;
// import org.modelmapper.ModelMapper;
// import org.springframework.stereotype.Service;

// import java.util.List;
// import java.util.Calendar;
// import java.util.Date;
// import java.util.stream.Collectors;

// @Service
// @RequiredArgsConstructor
// @Slf4j
// public class ChildUserService {

//     private final ChildUserRepository childUserRepository;
//     private final ModelMapper modelMapper;

//     public ChildUserDTO createChildUser(ChildUserDTO childUserDTO) {
//         Calendar calendar = Calendar.getInstance();
//         calendar.add(Calendar.YEAR, -3); // 5 years before today

//         if (childUserDTO.getDateOfBirth().after(calendar.getTime())) {
//             throw new IllegalArgumentException("Date of birth must be at least 3 years before today");
//         }
//         ChildUser childUser = modelMapper.map(childUserDTO, ChildUser.class);
//         // ✅ If activePlanId is set, assign planStartDate to today
//         if (childUser.getActivePlanId() != null) {
//             childUser.setPlanStartDate(new Date());
//         }
//         childUser = childUserRepository.save(childUser);
//         log.info("Created ChildUser with ID: {}", childUser.getChildId());
//         return modelMapper.map(childUser, ChildUserDTO.class);
//     }

//     public ChildUserDTO getChildUserById(String childId) {
//         ChildUser childUser = childUserRepository.findById(childId)
//                 .orElseThrow(() -> new ResourceNotFoundException("ChildUser not found with ID: " + childId));
//         return modelMapper.map(childUser, ChildUserDTO.class);
//     }

//     public List<ChildUserDTO> getAllChildUsers() {
//         List<ChildUser> childUsers = childUserRepository.findAll();
//         if (childUsers.isEmpty()) {
//             log.warn("No Child Users found in the database.");
//         }
//         return childUsers.stream()
//                 .map(childUser -> modelMapper.map(childUser, ChildUserDTO.class))
//                 .collect(Collectors.toList());
//     }

//     public ChildUserDTO updateChildUser(String childId, ChildUserDTO childUserDTO) {
//         ChildUser existingChildUser = childUserRepository.findById(childId)
//                 .orElseThrow(() -> new ResourceNotFoundException("ChildUser not found with ID: " + childId));

//         // ✅ Only update activePlanId if it's the only field provided
//         if (isOnlyActivePlanIdUpdated(childUserDTO)) {
//             existingChildUser.setActivePlanId(childUserDTO.getActivePlanId());  // Can be null
//             existingChildUser.setPlanStartDate(new Date());  // ✅ Set new start date if plan changes
//         } else {
//             // ✅ Update only non-null fields
//             updateNonNullFields(existingChildUser, childUserDTO);
//         }
//         existingChildUser = childUserRepository.save(existingChildUser);
//         log.info("Updated ChildUser with ID: {}", existingChildUser.getChildId());
//         return modelMapper.map(existingChildUser, ChildUserDTO.class);
//     }

//     // ✅ Checks if only activePlanId is provided (allows null values)
//     private boolean isOnlyActivePlanIdUpdated(ChildUserDTO dto) {
//         return dto.getChildName() == null &&
//                 dto.getSchoolId() == null &&
//                 dto.getGrade() == null &&
//                 dto.getDateOfBirth() == null &&
//                 dto.getAgeGroup() == null &&
//                 dto.getBloodGroup() == null &&
//                 dto.getPhoneNumber() == 0 &&
//                 dto.getPlanExpiryDate() == null &&
//                 dto.getPlanStartDate() == null;
//     }

//     // ✅ Updates only non-null fields from DTO
//     private void updateNonNullFields(ChildUser existing, ChildUserDTO dto) {
//         if (dto.getChildName() != null) existing.setChildName(dto.getChildName());
//         if (dto.getSchoolId() != null) existing.setSchoolId(dto.getSchoolId());
//         if (dto.getGrade() != null) existing.setGrade(dto.getGrade());
//         if (dto.getDateOfBirth() != null) existing.setDateOfBirth(dto.getDateOfBirth());
//         if (dto.getAgeGroup() != null) existing.setAgeGroup(dto.getAgeGroup());
//         if (dto.getBloodGroup() != null) existing.setBloodGroup(dto.getBloodGroup());
//         if (dto.getPhoneNumber() != 0) existing.setPhoneNumber(dto.getPhoneNumber());
//         if (dto.getPlanExpiryDate() != null) existing.setPlanExpiryDate(dto.getPlanExpiryDate());
//         if (dto.getActivePlanId() != null) existing.setActivePlanId(dto.getActivePlanId());  // ✅ Allows null
//     }

//     public void deleteChildUser(String childId) {
//         ChildUser childUser = childUserRepository.findById(childId)
//                 .orElseThrow(() -> new ResourceNotFoundException("ChildUser not found with ID: " + childId));
//         childUserRepository.delete(childUser);
//         log.info("Deleted ChildUser with ID: {}", childId);
//     }

//     public List<ChildUserDTO> findByPhoneNumber(long phoneNumber) {
//         log.info("Finding ChildUsers by phoneNumber: {}", phoneNumber);
//         List<ChildUser> childUsers = childUserRepository.findByPhoneNumber(phoneNumber);
//         if (childUsers.isEmpty()) {
//             throw new ResourceNotFoundException("No ChildUsers found with phone number: " + phoneNumber);
//         }
//         return childUsers.stream()
//                 .map(childUser -> modelMapper.map(childUser, ChildUserDTO.class))
//                 .collect(Collectors.toList());
//     }
// }


package com.sensei.backend.domain.service;

import java.util.List;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

import com.sensei.backend.adapter.persistence.jpa.entity.*;
import com.sensei.backend.application.dto.ChildUserDTO;
import com.sensei.backend.domain.port.repository.*;
import com.sensei.backend.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChildUserService {

    private final ChildUserRepository childUserRepository;
    private final ParentUserRepository parentUserRepository;
    private final PricingPlanRepository pricingPlanRepository;
    private final ModelMapper modelMapper;

    // ---------------- CREATE CHILD ----------------
    public ChildUserDTO createChild(ChildUserDTO dto) {

        log.info("CHILD_CREATE_REQUEST_RECEIVED",
                org.slf4j.MDC.getCopyOfContextMap()
        );

        ParentUser parent = parentUserRepository.findById(dto.getParentId())
                .orElseThrow(() -> {
                    log.warn("PARENT_NOT_FOUND",
                            org.slf4j.MDC.getCopyOfContextMap()
                    );
                    return new RuntimeException("Parent not found");
                });

        ChildUser child = new ChildUser();
        child.setChildName(dto.getChildName());
        child.setGender(dto.getGender());
        child.setGrade(dto.getGrade());
        child.setAgeGroup(dto.getAgeGroup());
        child.setBloodGroup(dto.getBloodGroup());
        child.setPhoneNumber(dto.getPhoneNumber());
        child.setDateOfBirth(dto.getDateOfBirth());
        child.setParentUser(parent);

        if (dto.getActivePlanId() != null) {
            PricingPlan plan = pricingPlanRepository.findById(dto.getActivePlanId())
                    .orElseThrow(() -> {
                        log.warn("PRICING_PLAN_NOT_FOUND",
                                org.slf4j.MDC.getCopyOfContextMap()
                        );
                        return new RuntimeException("Plan not found");
                    });

            child.setActivePlanId(plan.getId());
            child.setPlanStartDate(new Date());
        }

        ChildUser saved = childUserRepository.save(child);

        log.info("CHILD_CREATED_SUCCESSFULLY",
                org.slf4j.MDC.getCopyOfContextMap()
        );

        return modelMapper.map(saved, ChildUserDTO.class);
    }

    // ---------------- GET CHILDREN BY PARENT ----------------
    public List<ChildUserDTO> getChildrenByParent(UUID parentId) {

        log.info("FETCH_CHILDREN_BY_PARENT",
                org.slf4j.MDC.getCopyOfContextMap()
        );

        return childUserRepository.findByParentUser_ParentId(parentId)
                .stream()
                .map(c -> modelMapper.map(c, ChildUserDTO.class))
                .collect(Collectors.toList());
    }

    // ---------------- GET CHILD BY ID ----------------
    public ChildUserDTO getByChildId(UUID childId) {

        log.info("FETCH_CHILD_BY_ID",
                org.slf4j.MDC.getCopyOfContextMap()
        );

        ChildUser child = childUserRepository.findByChildId(childId)
                .orElseThrow(() -> {
                    log.warn("CHILD_NOT_FOUND",
                            org.slf4j.MDC.getCopyOfContextMap()
                    );
                    return new ResourceNotFoundException("Child not found");
                });

        return modelMapper.map(child, ChildUserDTO.class);
    }

    // ---------------- UPDATE CHILD ----------------
    public ChildUserDTO update(UUID childId, ChildUserDTO dto) {

        log.info("CHILD_UPDATE_REQUEST",
                org.slf4j.MDC.getCopyOfContextMap()
        );

        ChildUser existing = childUserRepository.findById(childId)
                .orElseThrow(() -> {
                    log.warn("CHILD_NOT_FOUND_FOR_UPDATE",
                            org.slf4j.MDC.getCopyOfContextMap()
                    );
                    return new ResourceNotFoundException("Child not found");
                });

        modelMapper.map(dto, existing);

        if (dto.getActivePlanId() != null) {
            existing.setPlanStartDate(new Date());
        }

        ChildUser updated = childUserRepository.save(existing);

        log.info("CHILD_UPDATED_SUCCESSFULLY",
                org.slf4j.MDC.getCopyOfContextMap()
        );

        return modelMapper.map(updated, ChildUserDTO.class);
    }

    // ---------------- DELETE CHILD ----------------
    public void delete(UUID childId) {

        log.info("CHILD_DELETE_REQUEST",
                org.slf4j.MDC.getCopyOfContextMap()
        );

        childUserRepository.deleteById(childId);

        log.info("CHILD_DELETED_SUCCESSFULLY",
                org.slf4j.MDC.getCopyOfContextMap()
        );
    }

    // ---------------- SEARCH BY PHONE ----------------
    public List<ChildUserDTO> findByPhone(String phone) {

        log.info("SEARCH_CHILD_BY_PHONE",
                org.slf4j.MDC.getCopyOfContextMap()
        );

        return childUserRepository.findByPhoneNumber(phone)
                .stream()
                .map(c -> modelMapper.map(c, ChildUserDTO.class))
                .collect(Collectors.toList());
    }

    // ---------------- GET ALL ----------------
    public List<ChildUserDTO> getAllChildren() {

        log.info("FETCH_ALL_CHILDREN",
                org.slf4j.MDC.getCopyOfContextMap()
        );

        return childUserRepository.findAll()
                .stream()
                .map(c -> modelMapper.map(c, ChildUserDTO.class))
                .collect(Collectors.toList());
    }
}
