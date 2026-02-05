// 

package com.sensei.backend.service;
import com.sensei.backend.dto.subject.SubjectRequestDTO;
import com.sensei.backend.dto.subject.SubjectResponseDTO;

import java.util.List;
import java.util.UUID;

public interface SubjectService {

    // SubjectResponseDTO createSubject(SubjectRequestDTO dto);

    // List<SubjectResponseDTO> getAllSubjects();

    // SubjectResponseDTO getSubjectById(UUID id);

    // void deleteSubject(UUID id);
    SubjectResponseDTO create(SubjectRequestDTO dto);

    List<SubjectResponseDTO> getAll();

    SubjectResponseDTO getById(UUID id);

    SubjectResponseDTO update(UUID id, SubjectRequestDTO dto);

    void delete(UUID id);   // soft delete
}
