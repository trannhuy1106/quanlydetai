package com.ute.quanlydetai.repository;

import com.ute.quanlydetai.entity.KetQuaDeTai;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KetQuaDeTaiRepository
        extends JpaRepository<KetQuaDeTai, Long> {

    Optional<KetQuaDeTai> findByDeTaiId(Long deTaiId);
}