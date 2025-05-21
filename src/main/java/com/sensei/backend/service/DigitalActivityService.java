package com.sensei.backend.service;

import com.sensei.backend.dto.DigitalActivityDTO;
import com.sensei.backend.entity.DigitalActivity;
import com.sensei.backend.repository.DigitalActivityRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DigitalActivityService {

    @Autowired
    private DigitalActivityRepository digitalActivityRepository;

    @Autowired
    private ModelMapper modelMapper;

    public DigitalActivityDTO createDigitalActivity(DigitalActivityDTO digitalActivityDTO) {
        DigitalActivity digitalActivity = modelMapper.map(digitalActivityDTO, DigitalActivity.class);
        DigitalActivity savedDigitalActivity = digitalActivityRepository.save(digitalActivity);
        return modelMapper.map(savedDigitalActivity, DigitalActivityDTO.class);
    }

    public List<DigitalActivityDTO> getAllDigitalActivities() {
        return digitalActivityRepository.findAll().stream()
                .map(digitalActivity -> modelMapper.map(digitalActivity, DigitalActivityDTO.class))
                .collect(Collectors.toList());
    }

    public Optional<DigitalActivityDTO> getDigitalActivityById(String id) {
        return digitalActivityRepository.findById(id)
                .map(digitalActivity -> modelMapper.map(digitalActivity, DigitalActivityDTO.class));
    }

    public DigitalActivityDTO updateDigitalActivity(String id, DigitalActivityDTO digitalActivityDTO) {
        DigitalActivity digitalActivity = modelMapper.map(digitalActivityDTO, DigitalActivity.class);
        digitalActivity.setDigitalActivityId(id);
        DigitalActivity updatedDigitalActivity = digitalActivityRepository.save(digitalActivity);
        return modelMapper.map(updatedDigitalActivity, DigitalActivityDTO.class);
    }

    public void deleteDigitalActivity(String id) {
        digitalActivityRepository.deleteById(id);
    }
}
