package com.sensei.backend.service;

import com.sensei.backend.dto.SubjectDTO;
import com.sensei.backend.entity.Subject;
import com.sensei.backend.repository.SubjectRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private ModelMapper modelMapper;

    public SubjectDTO createSubject(SubjectDTO subjectDTO) {
        Subject subject = modelMapper.map(subjectDTO, Subject.class);
        Subject savedSubject = subjectRepository.save(subject);
        return modelMapper.map(savedSubject, SubjectDTO.class);
    }

    public List<SubjectDTO> getAllSubjects() {
        return subjectRepository.findAll().stream()
                .map(subject -> modelMapper.map(subject, SubjectDTO.class))
                .collect(Collectors.toList());
    }

    public Optional<SubjectDTO> getSubjectById(String id) {
        return subjectRepository.findById(id)
                .map(subject -> modelMapper.map(subject, SubjectDTO.class));
    }

    public SubjectDTO updateSubject(String id, SubjectDTO subjectDTO) {
        Subject subject = modelMapper.map(subjectDTO, Subject.class);
        subject.setSubjectId(id);
        Subject updatedSubject = subjectRepository.save(subject);
        return modelMapper.map(updatedSubject, SubjectDTO.class);
    }

    public void deleteSubject(String id) {
        subjectRepository.deleteById(id);
    }
}
