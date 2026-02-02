package com.sensei.backend.domain.port.repository.Module;

import com.sensei.backend.domain.model.ModuleModel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ModuleRepositoryPort {

    ModuleModel save(ModuleModel module);

    Optional<ModuleModel> findById(UUID id);

    List<ModuleModel> findActive();

}
