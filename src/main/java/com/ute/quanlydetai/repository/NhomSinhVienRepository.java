package com.ute.quanlydetai.repository;

import com.ute.quanlydetai.entity.NhomSinhVien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NhomSinhVienRepository extends JpaRepository<NhomSinhVien, Long> {
}