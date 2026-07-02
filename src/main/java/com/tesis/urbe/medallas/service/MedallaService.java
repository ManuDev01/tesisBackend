package com.tesis.urbe.medallas.service;

import com.tesis.urbe.medallas.repository.MedallaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.*;

@Service
public class MedallaService {

    @Autowired
    private MedallaRepository medallaRepository;


}
