package com.tesis.urbe.medallas.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.tesis.urbe.medallas.entity.MedallasEntity;

public interface MedallasRepository extends JpaRepository<MedallasEntity, Long> {
    
}
