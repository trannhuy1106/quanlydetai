package com.ute.quanlydetai.repository;

import com.ute.quanlydetai.entity.DiemDanhGia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiemDanhGiaRepository
        extends JpaRepository<DiemDanhGia, Long> {

    List<DiemDanhGia> findByDeTaiId(Long deTaiId);

    boolean existsByDeTaiIdAndGiangVienId(
            Long deTaiId,
            Long giangVienId
    );
}