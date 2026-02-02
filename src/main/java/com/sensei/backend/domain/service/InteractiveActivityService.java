// package com.sensei.backend.service;

// import com.sensei.backend.dto.InteractiveActivityDTO;
// import com.sensei.backend.entity.InteractiveActivity;
// import com.sensei.backend.repository.InteractiveActivityRepository;
// import org.modelmapper.ModelMapper;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// import java.util.List;
// import java.util.Optional;
// import java.util.stream.Collectors;

// @Service
// public class InteractiveActivityService {

//     @Autowired
//     private InteractiveActivityRepository interactiveActivityRepository;

//     @Autowired
//     private ModelMapper modelMapper;

//     public InteractiveActivityDTO createInteractiveActivity(InteractiveActivityDTO interactiveActivityDTO) {
//         InteractiveActivity interactiveActivity = modelMapper.map(interactiveActivityDTO, InteractiveActivity.class);
//         InteractiveActivity savedInteractiveActivity = interactiveActivityRepository.save(interactiveActivity);
//         return modelMapper.map(savedInteractiveActivity, InteractiveActivityDTO.class);
//     }

//     public List<InteractiveActivityDTO> getAllInteractiveActivities() {
//         return interactiveActivityRepository.findAll().stream()
//                 .map(interactiveActivity -> modelMapper.map(interactiveActivity, InteractiveActivityDTO.class))
//                 .collect(Collectors.toList());
//     }

//     public Optional<InteractiveActivityDTO> getInteractiveActivityById(String id) {
//         return interactiveActivityRepository.findById(id)
//                 .map(interactiveActivity -> modelMapper.map(interactiveActivity, InteractiveActivityDTO.class));
//     }

//     public InteractiveActivityDTO updateInteractiveActivity(String id, InteractiveActivityDTO interactiveActivityDTO) {
//         InteractiveActivity interactiveActivity = modelMapper.map(interactiveActivityDTO, InteractiveActivity.class);
//         interactiveActivity.setInteractiveActivityId(id);
//         InteractiveActivity updatedInteractiveActivity = interactiveActivityRepository.save(interactiveActivity);
//         return modelMapper.map(updatedInteractiveActivity, InteractiveActivityDTO.class);
//     }

//     public void deleteInteractiveActivity(String id) {
//         interactiveActivityRepository.deleteById(id);
//     }
// }
package com.sensei.backend.domain.service;

import java.util.List;
import java.util.UUID;

import com.sensei.backend.application.dto.interactiveactivity.InteractiveActivityRequestDTO;
import com.sensei.backend.application.dto.interactiveactivity.InteractiveActivityResponseDTO;

public interface InteractiveActivityService {

    InteractiveActivityResponseDTO create(InteractiveActivityRequestDTO dto);

    List<InteractiveActivityResponseDTO> getBySubModule(UUID subModuleId);

    InteractiveActivityResponseDTO getById(UUID id);

    InteractiveActivityResponseDTO update(UUID id, InteractiveActivityRequestDTO dto);

    void delete(UUID id);
}
