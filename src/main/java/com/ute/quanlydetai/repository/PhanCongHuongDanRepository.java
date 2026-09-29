package com.ute.quanlydetai.repository;

import com.ute.quanlydetai.entity.PhanCongHuongDan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhanCongHuongDanRepository
        extends JpaRepository<PhanCongHuongDan, Long> {

    long countByDeTaiId(Long deTaiId);

    boolean existsByDeTaiIdAndGiangVienId(
            Long deTaiId,
            Long giangVienId
    );
}