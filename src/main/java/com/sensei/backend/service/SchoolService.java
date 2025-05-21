package com.sensei.backend.service;

import com.sensei.backend.dto.SchoolDTO;
import com.sensei.backend.entity.School;
import com.sensei.backend.exception.ResourceNotFoundException;
import com.sensei.backend.repository.SchoolRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SchoolService {

    private final SchoolRepository schoolRepository;
    private final ModelMapper modelMapper;

    public SchoolDTO createSchool(SchoolDTO schoolDTO) {
        School school = modelMapper.map(schoolDTO, School.class);
        school = schoolRepository.save(school);
        log.info("School created: {}", school.getSchoolId());
        return modelMapper.map(school, SchoolDTO.class);
    }

    public SchoolDTO getSchoolById(String id) {
        School school = schoolRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("School not found with id " + id));
        return modelMapper.map(school, SchoolDTO.class);
    }

    public List<SchoolDTO> getAllSchools() {
        return schoolRepository.findAll().stream()
                .map(school -> modelMapper.map(school, SchoolDTO.class))
                .collect(Collectors.toList());
    }

    public SchoolDTO updateSchool(String id, SchoolDTO schoolDTO) {
        School existingSchool = schoolRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("School not found with id " + id));

        modelMapper.map(schoolDTO, existingSchool);
        schoolRepository.save(existingSchool);
        log.info("School updated: {}", id);
        return modelMapper.map(existingSchool, SchoolDTO.class);
    }

    public void deleteSchool(String id) {
        School existingSchool = schoolRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("School not found with id " + id));
        schoolRepository.delete(existingSchool);
        log.info("School deleted: {}", id);
    }
}
