package com.ute.quanlydetai.repository;

import com.ute.quanlydetai.entity.PhanCongPhanBien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhanCongPhanBienRepository
        extends JpaRepository<PhanCongPhanBien, Long> {

    boolean existsByDeTaiIdAndGiangVienId(
            Long deTaiId,
            Long giangVienId
    );
}