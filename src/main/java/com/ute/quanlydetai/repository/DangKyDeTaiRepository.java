package com.ute.quanlydetai.repository;

import com.ute.quanlydetai.entity.DangKyDeTai;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DangKyDeTaiRepository
        extends JpaRepository<DangKyDeTai, Long> {

    boolean existsByNhomIdAndDotDangKyId(
            Long nhomId,
            Long dotDangKyId
    );

    Optional<DangKyDeTai> findFirstByNhomIdAndTrangThaiOrderByIdDesc(
            Long nhomId,
            String trangThai
    );
}