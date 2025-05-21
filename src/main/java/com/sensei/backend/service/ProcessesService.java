package com.sensei.backend.service;

import com.sensei.backend.dto.ProcessesDTO;
import com.sensei.backend.entity.Processes;
import com.sensei.backend.repository.ProcessesRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProcessesService {

    @Autowired
    private ProcessesRepository processesRepository;

    @Autowired
    private ModelMapper modelMapper;

    public ProcessesDTO createProcesses(ProcessesDTO processesDTO) {
        Processes processes = modelMapper.map(processesDTO, Processes.class);
        Processes savedProcesses = processesRepository.save(processes);
        return modelMapper.map(savedProcesses, ProcessesDTO.class);
    }

    public List<ProcessesDTO> getAllProcesses() {
        return processesRepository.findAll().stream()
                .map(processes -> modelMapper.map(processes, ProcessesDTO.class))
                .collect(Collectors.toList());
    }

    public Optional<ProcessesDTO> getProcessesById(String id) {
        return processesRepository.findById(id)
                .map(processes -> modelMapper.map(processes, ProcessesDTO.class));
    }

    public ProcessesDTO updateProcesses(String id, ProcessesDTO processesDTO) {
        Processes processes = modelMapper.map(processesDTO, Processes.class);
        processes.setProcessId(id);
        Processes updatedProcesses = processesRepository.save(processes);
        return modelMapper.map(updatedProcesses, ProcessesDTO.class);
    }

    public void deleteProcesses(String id) {
        processesRepository.deleteById(id);
    }
}
