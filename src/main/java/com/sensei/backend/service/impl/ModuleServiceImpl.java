// package com.sensei.backend.service.impl;

// import com.sensei.backend.dto.module.ModuleRequestDTO;
// import com.sensei.backend.dto.module.ModuleResponseDTO;
// import com.sensei.backend.entity.Module;
// import com.sensei.backend.entity.Subject;
// import com.sensei.backend.repository.ModuleRepository;
// import com.sensei.backend.repository.SubjectRepository;
// import com.sensei.backend.service.ModuleService;
// import lombok.RequiredArgsConstructor;
// import org.springframework.stereotype.Service;

// import java.util.List;
// import java.util.UUID;
// import java.util.stream.Collectors;

// @Service
// @RequiredArgsConstructor
// public class ModuleServiceImpl implements ModuleService {

//     private final ModuleRepository moduleRepository;
//     private final SubjectRepository subjectRepository;

//     @Override
//     public ModuleResponseDTO createModule(ModuleRequestDTO dto) {

//         Subject subject = subjectRepository.findById(dto.getSubjectId())
//                 .orElseThrow(() -> new RuntimeException("Subject not found"));

//         Module module = Module.builder()
//                 .subject(subject)
//                 .name(dto.getName())
//                 .description(dto.getDescription())
//                 .orderIndex(dto.getOrderIndex())
//                 .isActive(true)
//                 .build();

//         Module saved = moduleRepository.save(module);
//         return map(saved);
//     }

//     @Override
//     public List<ModuleResponseDTO> getModulesBySubject(UUID subjectId) {

//         Subject subject = subjectRepository.findById(subjectId)
//                 .orElseThrow(() -> new RuntimeException("Subject not found"));

//         return moduleRepository.findBySubject(subject)
//                 .stream()
//                 .map(this::map)
//                 .collect(Collectors.toList());
//     }

//     private ModuleResponseDTO map(Module module) {
//         return ModuleResponseDTO.builder()
//                 .id(module.getId())
//                 .subjectId(module.getSubject().getId())
//                 .name(module.getName())
//                 .description(module.getDescription())
//                 .orderIndex(module.getOrderIndex())
//                 .isActive(module.getIsActive())
//                 .createdAt(module.getCreatedAt())
//                 .build();
//     }
// }
package com.sensei.backend.service.impl;

import com.sensei.backend.dto.module.*;
import com.sensei.backend.entity.Module;
import com.sensei.backend.entity.Subject;
import com.sensei.backend.repository.ModuleRepository;
import com.sensei.backend.repository.SubjectRepository;
import com.sensei.backend.service.ModuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ModuleServiceImpl implements ModuleService {

    private final ModuleRepository moduleRepository;
    private final SubjectRepository subjectRepository;

    @Override
    public ModuleResponseDTO createModule(ModuleRequestDTO dto) {
        Subject subject = subjectRepository.findById(dto.getSubjectId())
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        Module module = Module.builder()
                .subject(subject)
                .name(dto.getName())
                .description(dto.getDescription())
                .orderIndex(dto.getOrderIndex())
                .isActive(true)
                .build();

        return map(moduleRepository.save(module));
    }

    @Override
    public List<ModuleResponseDTO> getModulesBySubject(UUID subjectId) {
        return moduleRepository
                .findBySubjectIdAndIsActiveTrueOrderByOrderIndexAsc(subjectId)
                .stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    @Override
    public ModuleResponseDTO getById(UUID id) {
        return map(moduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Module not found")));
    }

    @Override
    public ModuleResponseDTO update(UUID id, ModuleRequestDTO dto) {
        Module module = moduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Module not found"));

        module.setName(dto.getName());
        module.setDescription(dto.getDescription());
        module.setOrderIndex(dto.getOrderIndex());

        return map(moduleRepository.save(module));
    }

    @Override
    public void delete(UUID id) {
        Module module = moduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Module not found"));

        module.setIsActive(false);
        moduleRepository.save(module);
    }

    private ModuleResponseDTO map(Module m) {
        return ModuleResponseDTO.builder()
                .id(m.getId())
                .name(m.getName())
                .description(m.getDescription())
                .orderIndex(m.getOrderIndex())
                .isActive(m.getIsActive())
                .subjectId(m.getSubject().getId())
                .subjectName(m.getSubject().getName())
                .build();
    }
    @Override
public List<ModuleResponseDTO> getAllModules() {
    return moduleRepository.findByIsActiveTrueOrderByCreatedAtDesc()
            .stream()
            .map(this::map)
            .collect(Collectors.toList());
}
}

