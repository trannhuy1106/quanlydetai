package com.ute.quanlydetai.repository;

import com.ute.quanlydetai.entity.DeTai;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeTaiRepository extends JpaRepository<DeTai, Long> {

    List<DeTai> findByTrangThai(String trangThai);
}