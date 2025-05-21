package com.sensei.backend.service;

import com.sensei.backend.dto.CounsellorDTO;
import com.sensei.backend.entity.Counsellor;
import com.sensei.backend.exception.ResourceNotFoundException;
import com.sensei.backend.repository.CounsellorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CounsellorService {

    private final CounsellorRepository counsellorRepository;
    private final ModelMapper modelMapper;

    public CounsellorDTO createCounsellor(CounsellorDTO counsellorDTO) {
        Counsellor counsellor = modelMapper.map(counsellorDTO, Counsellor.class);
        counsellor = counsellorRepository.save(counsellor);
        log.info("Counsellor created: {}", counsellor.getCounsellorId());
        return modelMapper.map(counsellor, CounsellorDTO.class);
    }

    public CounsellorDTO getCounsellorById(String id) {
        Counsellor counsellor = counsellorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Counsellor not found with id " + id));
        return modelMapper.map(counsellor, CounsellorDTO.class);
    }

    public List<CounsellorDTO> getAllCounsellors() {
        return counsellorRepository.findAll().stream()
                .map(counsellor -> modelMapper.map(counsellor, CounsellorDTO.class))
                .collect(Collectors.toList());
    }

    public CounsellorDTO updateCounsellor(String id, CounsellorDTO counsellorDTO) {
        Counsellor existingCounsellor = counsellorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Counsellor not found with id " + id));

        modelMapper.map(counsellorDTO, existingCounsellor);
        counsellorRepository.save(existingCounsellor);
        log.info("Counsellor updated: {}", id);
        return modelMapper.map(existingCounsellor, CounsellorDTO.class);
    }

    public void deleteCounsellor(String id) {
        Counsellor existingCounsellor = counsellorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Counsellor not found with id " + id));
        counsellorRepository.delete(existingCounsellor);
        log.info("Counsellor deleted: {}", id);
    }
}
