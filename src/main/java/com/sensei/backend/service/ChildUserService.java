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
package com.sensei.backend.service;
import java.util.List;
import java.time.LocalDate;
import java.util.Date;

import com.sensei.backend.dto.ChildUserDTO;
import com.sensei.backend.entity.*;
import com.sensei.backend.exception.ResourceNotFoundException;
import com.sensei.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChildUserService {

    private final ChildUserRepository childUserRepository;
    private final ParentUserRepository parentUserRepository;
    private final PricingPlanRepository pricingPlanRepository;
    private final ModelMapper modelMapper;

    public ChildUserDTO createChild(ChildUserDTO dto) {
        ChildUser child = new ChildUser();

        child.setChildName(dto.getChildName());
        child.setGender(dto.getGender());
        child.setGrade(dto.getGrade());
        child.setAgeGroup(dto.getAgeGroup());
        child.setBloodGroup(dto.getBloodGroup());
        child.setPhoneNumber(dto.getPhoneNumber());
        child.setDateOfBirth(dto.getDateOfBirth());

        ParentUser parent = parentUserRepository.findById(dto.getParentId())
                .orElseThrow(() -> new RuntimeException("Parent not found"));
        child.setParentUser(parent);

        if (dto.getActivePlanId() != null) {
            PricingPlan plan = pricingPlanRepository.findById(dto.getActivePlanId())
                    .orElseThrow(() -> new RuntimeException("Plan not found"));
            child.setActivePlanId(plan.getId());
            child.setPlanStartDate(LocalDate.now());

            child.setPlanStartDate(dto.getPlanStartDate());
        }

        return modelMapper.map(childUserRepository.save(child), ChildUserDTO.class);
    }
    // 🔹 All children of a parent
    public List<ChildUserDTO> getChildrenByParent(UUID parentId) {
    return childUserRepository.findByParentUser_ParentId(parentId)
            .stream()
            .map(c -> modelMapper.map(c, ChildUserDTO.class))
            .collect(Collectors.toList());
}

// 🔹 Safe child lookup
public ChildUserDTO getByChildId(UUID childId) {
    ChildUser child = childUserRepository.findByChildId(childId)
            .orElseThrow(() -> new ResourceNotFoundException("Child not found"));
    return modelMapper.map(child, ChildUserDTO.class);
}
public ChildUserDTO createChildUser(ChildUserDTO dto) {
    ChildUser child = modelMapper.map(dto, ChildUser.class);

    // auto set plan start if plan assigned
    if (child.getActivePlanId() != null) {
        child.setPlanStartDate(LocalDate.now());
    }

    child = childUserRepository.save(child);
    return modelMapper.map(child, ChildUserDTO.class);
}

public ChildUserDTO update(UUID childId, ChildUserDTO dto) {
    ChildUser existing = childUserRepository.findById(childId)
            .orElseThrow(() -> new ResourceNotFoundException("Child not found"));

    modelMapper.map(dto, existing);

    if (dto.getActivePlanId() != null) {
        existing.setPlanStartDate(LocalDate.now());
    }

    return modelMapper.map(childUserRepository.save(existing), ChildUserDTO.class);
}

public void delete(UUID childId) {
    childUserRepository.deleteById(childId);
}

public List<ChildUserDTO> findByPhone(String phone) {
    return childUserRepository.findByPhoneNumber(phone)
            .stream()
            .map(c -> modelMapper.map(c, ChildUserDTO.class))
            .collect(Collectors.toList());
}

public List<ChildUserDTO> getAllChildren() {
    return childUserRepository.findAll()
            .stream()
            .map(c -> modelMapper.map(c, ChildUserDTO.class))
            .collect(Collectors.toList());
}



}
