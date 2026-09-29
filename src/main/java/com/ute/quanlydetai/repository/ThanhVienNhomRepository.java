package com.ute.quanlydetai.repository;

import com.ute.quanlydetai.entity.ThanhVienNhom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThanhVienNhomRepository extends JpaRepository<ThanhVienNhom, Long> {

    long countByNhomId(Long nhomId);

    boolean existsBySinhVienId(Long sinhVienId);

    boolean existsByNhomIdAndVaiTro(Long nhomId, String vaiTro);

    ThanhVienNhom findBySinhVienId(Long sinhVienId);
}