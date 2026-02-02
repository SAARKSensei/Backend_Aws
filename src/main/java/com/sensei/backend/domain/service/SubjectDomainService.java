package com.sensei.backend.domain.service;

import com.sensei.backend.domain.model.SubjectModel;

public class SubjectDomainService {

    public SubjectModel create(
            String name,
            String description,
            String iconUrl
    ) {
        return new SubjectModel(
                null,
                name,
                description,
                iconUrl,
                true,
                null
        );
    }
}
