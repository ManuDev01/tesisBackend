package com.tesis.urbe.medallas.service.service;

import com.tesis.urbe.medallas.repository.MedallaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.*;

@Service
public class MedallaService {

    private final MedallaRepository medallaRepository;

    public MedallaService(MedallaRepository medallaRepository) {
        this.medallaRepository = medallaRepository;
    }

}
