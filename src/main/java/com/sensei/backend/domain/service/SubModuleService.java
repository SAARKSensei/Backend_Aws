// package com.sensei.backend.service;

// import com.sensei.backend.dto.SubModuleDTO;
// import com.sensei.backend.entity.SubModule;
// import com.sensei.backend.repository.SubModuleRepository;
// import org.modelmapper.ModelMapper;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// import java.util.List;
// import java.util.Optional;
// import java.util.stream.Collectors;

// @Service
// public class SubModuleService {

//     @Autowired
//     private SubModuleRepository subModuleRepository;

//     @Autowired
//     private ModelMapper modelMapper;

//     public SubModuleDTO createSubModule(SubModuleDTO subModuleDTO) {
//         SubModule subModule = modelMapper.map(subModuleDTO, SubModule.class);
//         SubModule savedSubModule = subModuleRepository.save(subModule);
//         return modelMapper.map(savedSubModule, SubModuleDTO.class);
//     }

//     public List<SubModuleDTO> getAllSubModules() {
//         return subModuleRepository.findAll().stream()
//                 .map(subModule -> modelMapper.map(subModule, SubModuleDTO.class))
//                 .collect(Collectors.toList());
//     }

//     public Optional<SubModuleDTO> getSubModuleById(String id) {
//         return subModuleRepository.findById(id)
//                 .map(subModule -> modelMapper.map(subModule, SubModuleDTO.class));
//     }

//     public SubModuleDTO updateSubModule(String id, SubModuleDTO subModuleDTO) {
//         SubModule subModule = modelMapper.map(subModuleDTO, SubModule.class);
//         subModule.setSubModuleId(id);
//         SubModule updatedSubModule = subModuleRepository.save(subModule);
//         return modelMapper.map(updatedSubModule, SubModuleDTO.class);
//     }

//     public void deleteSubModule(String id) {
//         subModuleRepository.deleteById(id);
//     }
// }
package com.sensei.backend.domain.service;

import java.util.List;
import java.util.UUID;

import com.sensei.backend.application.dto.submodule.SubModuleRequestDTO;
import com.sensei.backend.application.dto.submodule.SubModuleResponseDTO;

public interface SubModuleService {

    SubModuleResponseDTO create(SubModuleRequestDTO dto);

    List<SubModuleResponseDTO> getAll();

    List<SubModuleResponseDTO> getByModule(UUID moduleId);

    SubModuleResponseDTO getById(UUID id);

    SubModuleResponseDTO update(UUID id, SubModuleRequestDTO dto);

    void delete(UUID id);
}