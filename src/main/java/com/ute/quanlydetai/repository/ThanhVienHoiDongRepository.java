package com.ute.quanlydetai.repository;

import com.ute.quanlydetai.entity.ThanhVienHoiDong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ThanhVienHoiDongRepository
        extends JpaRepository<ThanhVienHoiDong, Long> {

    List<ThanhVienHoiDong> findByHoiDongId(Long hoiDongId);

    Optional<ThanhVienHoiDong> findFirstByHoiDongIdAndVaiTro(
            Long hoiDongId,
            String vaiTro
    );
}