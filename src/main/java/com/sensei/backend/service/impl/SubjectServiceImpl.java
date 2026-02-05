// package com.sensei.backend.service.impl;

// import com.sensei.backend.dto.subject.SubjectRequestDTO;
// import com.sensei.backend.dto.subject.SubjectResponseDTO;


// import com.sensei.backend.entity.Subject;
// import com.sensei.backend.repository.SubjectRepository;
// import com.sensei.backend.service.SubjectService;
// import lombok.RequiredArgsConstructor;
// import org.springframework.stereotype.Service;

// import java.util.List;
// import java.util.UUID;
// import java.util.stream.Collectors;

// @Service
// @RequiredArgsConstructor
// public class SubjectServiceImpl implements SubjectService {

//     private final SubjectRepository subjectRepository;

//     @Override
//     public SubjectResponseDTO createSubject(SubjectRequestDTO dto) {

//         Subject subject = Subject.builder()
//                 .name(dto.getName())
//                 .description(dto.getDescription())
//                 .iconUrl(dto.getIconUrl())
//                 .isActive(true)
//                 .build();

//         Subject saved = subjectRepository.save(subject);
//         return mapToResponse(saved);
//     }

//     @Override
//     public List<SubjectResponseDTO> getAllSubjects() {
//         return subjectRepository.findAll()
//                 .stream()
//                 .map(this::mapToResponse)
//                 .collect(Collectors.toList());
//     }

//     @Override
//     public SubjectResponseDTO getSubjectById(UUID id) {
//         Subject subject = subjectRepository.findById(id)
//                 .orElseThrow(() -> new RuntimeException("Subject not found"));
//         return mapToResponse(subject);
//     }

//     @Override
//     public void deleteSubject(UUID id) {
//         subjectRepository.deleteById(id);
//     }

//     private SubjectResponseDTO mapToResponse(Subject subject) {
//         return SubjectResponseDTO.builder()
//                 .id(subject.getId())
//                 .name(subject.getName())
//                 .description(subject.getDescription())
//                 .iconUrl(subject.getIconUrl())
//                 .isActive(subject.getIsActive())
//                 .createdAt(subject.getCreatedAt())
//                 .build();
//     }
// }
package com.sensei.backend.service.impl;

import com.sensei.backend.dto.subject.SubjectRequestDTO;
import com.sensei.backend.dto.subject.SubjectResponseDTO;
import com.sensei.backend.entity.Subject;
import com.sensei.backend.repository.SubjectRepository;
import com.sensei.backend.service.SubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubjectServiceImpl implements SubjectService {

    private final SubjectRepository subjectRepository;

    @Override
    public SubjectResponseDTO create(SubjectRequestDTO dto) {
        Subject subject = Subject.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .iconUrl(dto.getIconUrl())
                .isActive(true)
                .build();

        return mapToResponse(subjectRepository.save(subject));
    }

    @Override
    public List<SubjectResponseDTO> getAll() {
        return subjectRepository.findByIsActiveTrue()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public SubjectResponseDTO getById(UUID id) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subject not found"));
        return mapToResponse(subject);
    }

    @Override
    public SubjectResponseDTO update(UUID id, SubjectRequestDTO dto) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        subject.setName(dto.getName());
        subject.setDescription(dto.getDescription());
        subject.setIconUrl(dto.getIconUrl());

        return mapToResponse(subjectRepository.save(subject));
    }

    @Override
    public void delete(UUID id) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subject not found"));
        subject.setIsActive(false);
        subjectRepository.save(subject);
    }

    private SubjectResponseDTO mapToResponse(Subject s) {
        return SubjectResponseDTO.builder()
                .id(s.getId())
                .name(s.getName())
                .description(s.getDescription())
                .iconUrl(s.getIconUrl())
                .isActive(s.getIsActive())
                .createdAt(s.getCreatedAt())
                .build();
    }
}
