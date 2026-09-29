package com.ute.quanlydetai.repository;

import com.ute.quanlydetai.entity.BaoCaoDeTai;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BaoCaoDeTaiRepository extends JpaRepository<BaoCaoDeTai, Long> {
}