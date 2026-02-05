// package com.sensei.backend.service;

// import com.sensei.backend.dto.ModuleDTO;
// import com.sensei.backend.entity.Module;
// import com.sensei.backend.repository.ModuleRepository;
// import org.modelmapper.ModelMapper;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// import java.util.List;
// import java.util.Optional;
// import java.util.stream.Collectors;

// @Service
// public class ModuleService {

//     @Autowired
//     private ModuleRepository moduleRepository;

//     @Autowired
//     private ModelMapper modelMapper;

//     public ModuleDTO createModule(ModuleDTO moduleDTO) {
//         Module module = modelMapper.map(moduleDTO, Module.class);
//         Module savedModule = moduleRepository.save(module);
//         return modelMapper.map(savedModule, ModuleDTO.class);
//     }

//     public List<ModuleDTO> getAllModules() {
//         return moduleRepository.findAll().stream()
//                 .map(module -> modelMapper.map(module, ModuleDTO.class))
//                 .collect(Collectors.toList());
//     }

//     public Optional<ModuleDTO> getModuleById(String id) {
//         return moduleRepository.findById(id)
//                 .map(module -> modelMapper.map(module, ModuleDTO.class));
//     }

//     public ModuleDTO updateModule(String id, ModuleDTO moduleDTO) {
//         Module module = modelMapper.map(moduleDTO, Module.class);
//         module.setModuleId(id);
//         Module updatedModule = moduleRepository.save(module);
//         return modelMapper.map(updatedModule, ModuleDTO.class);
//     }

//     public void deleteModule(String id) {
//         moduleRepository.deleteById(id);
//     }
// }
// package com.sensei.backend.service;

// import com.sensei.backend.dto.module.ModuleRequestDTO;
// import com.sensei.backend.dto.module.ModuleResponseDTO;

// import java.util.List;
// import java.util.UUID;

// public interface ModuleService {

//     ModuleResponseDTO createModule(ModuleRequestDTO dto);

//     List<ModuleResponseDTO> getModulesBySubject(UUID subjectId);
// }
package com.sensei.backend.service;

import com.sensei.backend.dto.module.ModuleRequestDTO;
import com.sensei.backend.dto.module.ModuleResponseDTO;

import java.util.List;
import java.util.UUID;

public interface ModuleService {

    ModuleResponseDTO createModule(ModuleRequestDTO dto);

    List<ModuleResponseDTO> getModulesBySubject(UUID subjectId);

    ModuleResponseDTO getById(UUID id);

    ModuleResponseDTO update(UUID id, ModuleRequestDTO dto);

    void delete(UUID id);

    List<ModuleResponseDTO> getAllModules();
}